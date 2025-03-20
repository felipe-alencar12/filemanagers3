package com.studyings3.filemanagers3.service;

import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface GenerateFileService{

    ResponseEntity<GenerateFileResponse> generateFile();

    
}
