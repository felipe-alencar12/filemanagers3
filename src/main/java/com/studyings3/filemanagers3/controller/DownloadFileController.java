package com.studyings3.filemanagers3.controller;

import com.studyings3.filemanagers3.api.DownloadFile;
import com.studyings3.filemanagers3.service.DownloadFileService;
import com.studyings3.filemanagers3.service.GenerateFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/download")
public class DownloadFileController implements DownloadFile {

    private final DownloadFileService downloadFileService;

    @Override
    @RequestMapping("/file")
    public ResponseEntity<String> downloadFileWithPresignedUrl(String fileKey) {
        return downloadFileService.downloadFile(fileKey);
    }
}
