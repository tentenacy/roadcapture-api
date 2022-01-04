package com.untilled.roadcapture.api.client;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface UploadClient {

    void upload(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    String getFileUrl(String fileName);

    void deleteFiles(List<String> fileNames);

    void delete(String fileName);

    boolean doesFileExists(String filaName);
}
