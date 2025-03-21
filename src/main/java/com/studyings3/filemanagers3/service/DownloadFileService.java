package com.studyings3.filemanagers3.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DownloadFileService {

    ResponseEntity<String> downloadFile(String fileKey);

    ResponseEntity<List<String>> listFiles();

}
