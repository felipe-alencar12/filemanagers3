package com.studyings3.filemanagers3.serviceImpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.studyings3.filemanagers3.service.DownloadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public ResponseEntity<List<String>> listFiles() {
        try {
            // Create a request to list objects in the bucket
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName);

            // List the objects in the bucket
            var result = amazonS3.listObjectsV2(listObjectsV2Request);

            // Collect file names (keys) in a list
            List<String> fileNames = new ArrayList<>();
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                fileNames.add(objectSummary.getKey());
            }

            // Return the file names as a JSON response
            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of("Error fetching file names"));
        }
    }

}
