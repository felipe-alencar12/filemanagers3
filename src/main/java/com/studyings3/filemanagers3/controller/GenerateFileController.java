package com.studyings3.filemanagers3.controller;

import com.studyings3.filemanagers3.api.GenerateFile;
import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import com.studyings3.filemanagers3.service.GenerateFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/generate")
@AllArgsConstructor
public class GenerateFileController implements GenerateFile {

    private final GenerateFileService generateFileService;

    @Override
    @RequestMapping("/file")
    public ResponseEntity<GenerateFileResponse> generateFile() {
        generateFileService.generateFile();
        return null;
    }
}
