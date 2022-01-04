package com.untilled.roadcapture.api.service.cloud;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.untilled.roadcapture.api.client.UploadClient;
import com.untilled.roadcapture.api.exception.io.CFileConvertFailedException;
import com.untilled.roadcapture.api.exception.io.CInvalidFileFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final UploadClient s3Service;

    //Multipart를 통해 전송된 파일 업로드
    public String uploadImage(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new CFileConvertFailedException();
        }
        return s3Service.getFileUrl(fileName);
    }

    //기존 확장자명 유지한 채 유니크한 파일의 이름 생성
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일 확장자명 가져옴
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new CInvalidFileFormatException();
        }
    }
}
