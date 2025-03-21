package com.studyings3.filemanagers3.serviceImpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.studyings3.filemanagers3.service.DownloadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public  class DownloadFileServiceImpl implements DownloadFileService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public ResponseEntity<String> downloadFile(String fileKey) {
        // Set expiration time (e.g., 1 hour from now)
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileKey)
                        .withMethod(com.amazonaws.HttpMethod.GET)
                        .withExpiration(expiration);

        // Generate the URL
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        // Return the URL as a ResponseEntity
        return ResponseEntity.ok(url.toString());
    }

}
