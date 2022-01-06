package com.untilled.roadcapture.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.untilled.roadcapture.api.dto.album.*;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.exception.business.*;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException.CMultiPartKeyMismatchException;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException.CPictureMultipartRequired;
import com.untilled.roadcapture.api.exception.business.CInvalidValueException.CThumbnailNonUniqueException;
import com.untilled.roadcapture.api.exception.io.CIOException.CCloudCommunicationException;
import com.untilled.roadcapture.api.exception.io.CIOException.CFileConvertFailedException;
import com.untilled.roadcapture.api.service.AlbumService;
import com.untilled.roadcapture.api.service.cloud.FileUploadService;
import com.untilled.roadcapture.domain.picture.Picture;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.util.CUrlUtils;
import com.untilled.roadcapture.util.validator.CustomCollectionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumApiController {

    private final CustomCollectionValidator validator;
    private final AlbumService albumService;
    private final FileUploadService fileUploadService;
    private final ObjectMapper mapper;

    @GetMapping("/users/albums")
    public Page<UserAlbumsResponse> userAlbums(@Validated UserAlbumsCondition cond, Pageable pageable) {
        return albumService.getUserAlbums(cond, pageable);
    }


    @GetMapping("/albums")
    public Page<AlbumsResponse> albums(@Validated AlbumsCondition cond, Pageable pageable) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return albumService.getAlbums(cond, pageable, user.getId());
    }

    @GetMapping("/followers/to/albums")
    public Page<AlbumsResponse> followingAlbums(@Validated FollowingAlbumsCondition cond, Pageable pageable) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return albumService.getFollowingAlbums(cond, pageable, user.getId());
    }

    @GetMapping("/albums/{albumId}")
    public AlbumResponse album(@PathVariable Long albumId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return albumService.getAlbum(albumId, user.getId());
    }

    @PutMapping("/albums/{albumId}")
    public void update(@PathVariable Long albumId, @ModelAttribute AlbumMultiPartRequest request, BindingResult bindingResult) throws BindException, JsonProcessingException {

        List<String> uploadedFiles = new ArrayList<>();
        AlbumUpdateRequest albumUpdateRequest = mapper.readValue(request.getData(), AlbumUpdateRequest.class);

        validateAlbumUpdateRequest(bindingResult, albumUpdateRequest);

        //썸네일이 유일한 지 확인
        checkThumbnailUnique(albumUpdateRequest.getPictures().stream()
                .map(pictureUpdateRequest -> pictureUpdateRequest.toEntity()).collect(Collectors.toList()));

        //사진을 생성하기 위한 파일이 있는 지 확인
        checkPictureMultipartRequired(request.getImages(), albumUpdateRequest.getPictures());

        //불일치하는 키가 있는 지 확인
        checkMultiPartKeyMismatch(request.getImages(), albumUpdateRequest.getPictures().stream()
                .map(picture -> picture.toEntity()).collect(Collectors.toList()));

        //요청 파일이 있을 때만 업로드
        if(!ObjectUtils.isEmpty(request.getImages())) {
            albumUpdateRequest.getPictures().stream().forEach(picture -> uploadIfMultipartKeyMatched(request.getImages(), uploadedFiles, picture));
        } else {
            albumUpdateRequest.getPictures().forEach(picture -> picture.imageUrlNotUpdatable());
        }

        //서비스에서 오류가 발생하면 업로드한 사진 모두 삭제
        updateRollbackable(albumId, uploadedFiles, albumUpdateRequest);
    }

    @PutMapping("/albums/{albumId}/temp")
    public void tempUpdate(@PathVariable Long albumId, @Validated @RequestBody TempAlbumUpdateRequest request, BindingResult bindingResult) throws BindException {

        validateTempAlbumUpdateRequest(bindingResult, request);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        albumService.tempUpdate(albumId, request);
    }

    @PostMapping("/albums")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@ModelAttribute AlbumMultiPartRequest request, BindingResult bindingResult) throws IOException, BindException {

        List<String> uploadedFiles = new ArrayList<>();
        AlbumCreateRequest albumCreateRequest = mapper.readValue(request.getData(), AlbumCreateRequest.class);

        validateAlbumCreateRequest(bindingResult, albumCreateRequest);

        //썸네일이 유일한 지 확인
        checkThumbnailUnique(albumCreateRequest.getPictures().stream()
                .map(pictureCreateRequest -> pictureCreateRequest.toEntity())
                .collect(Collectors.toList()));

        //사진을 생성하기 위한 파일이 있는 지 확인
        checkPictureMultipartRequired(request.getImages(), albumCreateRequest.getPictures().stream().map(picture -> picture.toPictureUpdateRequest()).collect(Collectors.toList()));
        //불일치하는 키가 있는 지 확인
        checkMultiPartKeyMismatch(request.getImages(), albumCreateRequest.getPictures().stream().map(picture -> picture.toEntity()).collect(Collectors.toList()));

        //이미지 업로드
        //만약 업로드 중 오류가 발생하면 기존에 업로드한 사진 모두 삭제
        albumCreateRequest.getPictures().stream().forEach(picture -> {
            String uploadedImageUrl = uploadRollbackable(request.getImages(), picture.toEntity(), uploadedFiles);
            picture.updateImageUrl(uploadedImageUrl);
            uploadedFiles.add(uploadedImageUrl);
        });

        albumService.create(albumCreateRequest);
    }

    @PostMapping("/albums/temp")
    @ResponseStatus(HttpStatus.CREATED)
    public void tempCreate(@Validated @RequestBody TempAlbumCreateRequest request, BindingResult bindingResult) throws BindException {

        validateTempAlbumCreateRequest(bindingResult, request);

        //썸네일이 유일한 지 확인
        checkThumbnailUnique(request.getPictures().stream()
                .map(pictureCreateRequest -> pictureCreateRequest.toEntity())
                .collect(Collectors.toList()));

        albumService.tempCreate(request);
    }

    @DeleteMapping("/albums/{albumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long albumId) {
        List<String> delete = albumService.delete(albumId);
        fileUploadService.deleteFiles(delete);
    }

    private void updateRollbackable(Long albumId, List<String> uploadedFiles, AlbumUpdateRequest albumUpdateRequest) {
        try {
            List<String> fileNamesToDelete = albumService.update(albumId, albumUpdateRequest);
            if(fileNamesToDelete.size() > 0) {
                fileUploadService.deleteFiles(fileNamesToDelete);
            }
        } catch (CBusinessException e) {
            fileUploadService.deleteFiles(uploadedFiles);
            throw e;
        }
    }

    private String uploadRollbackable(Map<String, MultipartFile> imagesToUpload, Picture picture, List<String> uploadedFiles) {
        try {
            return fileUploadService.upload(imagesToUpload.get(picture.getCreatedAt().toString()));
        } catch (CCloudCommunicationException | CFileConvertFailedException e) {
            fileUploadService.deleteFiles(uploadedFiles);
            throw e;
        }
    }

    private void uploadIfMultipartKeyMatched(Map<String, MultipartFile> images, List<String> uploadedFiles, PictureUpdateRequest picture) {
        if(!ObjectUtils.isEmpty(images.get(picture.getCreatedAt().toString()))) {
            //업로드 중 오류가 발생하면 기존에 업로드한 사진 모두 삭제
            String uploadedImageUrl = uploadRollbackable(images, picture.toEntity(), uploadedFiles);
            picture.updateImageUrl(uploadedImageUrl);
            uploadedFiles.add(CUrlUtils.extractFileNameFrom(uploadedImageUrl));
        } else {
            picture.imageUrlNotUpdatable();
        }
    }

    private void checkPictureMultipartRequired(Map<String, MultipartFile> images, List<PictureUpdateRequest> pictures) {
        if ((!ObjectUtils.isEmpty(images) &&
                !pictures.stream()
                        .filter(picture -> ObjectUtils.isEmpty(picture.getId()))
                        .allMatch(picture -> !ObjectUtils.isEmpty(images.get(picture.getCreatedAt().toString())))) ||
            (ObjectUtils.isEmpty(images) &&
                pictures.stream()
                        .filter(picture -> ObjectUtils.isEmpty(picture.getId())).count() > 0L)
        ) {
            throw new CPictureMultipartRequired();
        }
    }

    private void checkThumbnailUnique(List<Picture> pictures) {
        if (pictures.stream().filter(picture -> picture.isThumbnail()).count() != 1L) {
            throw new CThumbnailNonUniqueException();
        }
    }

    private void checkMultiPartKeyMismatch(Map<String, MultipartFile> pictureFilesToUpload, List<Picture> picturesToUpload) {
        if(!ObjectUtils.isEmpty(pictureFilesToUpload) &&
                !pictureFilesToUpload.keySet().stream()
                        .allMatch(key -> picturesToUpload.stream().anyMatch(picture -> picture.getCreatedAt().toString().equals(key)))) {
            throw new CMultiPartKeyMismatchException();
        }
    }

    private void validateAlbumUpdateRequest(BindingResult bindingResult, AlbumUpdateRequest albumUpdateRequest) throws BindException {
        validator.validate(albumUpdateRequest.getPictures(), bindingResult);
        albumUpdateRequest.getPictures().stream()
                .forEach(picture -> {
                    validator.validate(picture.getPlace(), bindingResult);
                    validator.validate(picture.getPlace().getAddress(), bindingResult);
                });

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

    private void validateAlbumCreateRequest(BindingResult bindingResult, AlbumCreateRequest albumCreateRequest) throws BindException {
        validator.validate(albumCreateRequest.getPictures(), bindingResult);
        albumCreateRequest.getPictures().stream()
                .forEach(picture -> {
                    validator.validate(picture.getPlace(), bindingResult);
                    validator.validate(picture.getPlace().getAddress(), bindingResult);
                });

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

    private void validateTempAlbumUpdateRequest(BindingResult bindingResult, TempAlbumUpdateRequest albumUpdateRequest) throws BindException {
        validator.validate(albumUpdateRequest.getPictures(), bindingResult);
        albumUpdateRequest.getPictures().stream()
                .forEach(picture -> {
                    validator.validate(picture.getPlace(), bindingResult);
                    validator.validate(picture.getPlace().getAddress(), bindingResult);
                });

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

    private void validateTempAlbumCreateRequest(BindingResult bindingResult, TempAlbumCreateRequest albumCreateRequest) throws BindException {
        validator.validate(albumCreateRequest.getPictures(), bindingResult);
        albumCreateRequest.getPictures().stream()
                .forEach(picture -> {
                    validator.validate(picture.getPlace(), bindingResult);
                    validator.validate(picture.getPlace().getAddress(), bindingResult);
                });

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }
}
