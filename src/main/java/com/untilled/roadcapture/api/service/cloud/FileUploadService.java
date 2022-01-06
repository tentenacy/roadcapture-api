package com.untilled.roadcapture.api.service.cloud;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.untilled.roadcapture.api.client.UploadClient;
import com.untilled.roadcapture.api.exception.io.CIOException;
import com.untilled.roadcapture.api.exception.io.CIOException.CCloudCommunicationException;
import com.untilled.roadcapture.api.exception.io.CIOException.CFileConvertFailedException;
import com.untilled.roadcapture.api.exception.io.CIOException.CInvalidFileFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final UploadClient s3Service;

    //Multipart를 통해 전송된 파일 업로드
    public String upload(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.upload(inputStream, objectMetadata, fileName);
        } catch (SdkClientException e) {
            throw new CCloudCommunicationException();
        } catch (IOException e) {
            throw new CFileConvertFailedException();
        }
        return s3Service.getFileUrl(fileName);
    }

    public boolean doesFileExists(String fileName) {
        return s3Service.doesFileExists(fileName);
    }

    public void delete(String fileName) {
        try {
            s3Service.delete(fileName);
        } catch (SdkClientException e) {
            throw new CCloudCommunicationException();
        }
    }

    public void deleteFiles(List<String> fileNames) {
        try {
            s3Service.deleteFiles(fileNames);
        } catch (SdkClientException e) {
            throw new CCloudCommunicationException();
        }
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
