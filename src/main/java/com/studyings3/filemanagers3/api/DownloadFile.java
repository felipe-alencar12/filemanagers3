package com.studyings3.filemanagers3.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface DownloadFile {

    ResponseEntity<String> downloadFileWithPresignedUrl(@RequestParam String fileKey);

    public ResponseEntity<List<String>> listFiles();
}
