package com.studyings3.filemanagers3.controller;

import com.studyings3.filemanagers3.api.GenerateFile;
import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenerateFileController implements GenerateFile {

    public ResponseEntity<GenerateFileResponse> generateFile() {
        return null;
    }
}
