package com.studyings3.filemanagers3.serviceImpl;

import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import com.studyings3.filemanagers3.service.GenerateFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class GenerateFileServiceImpl implements GenerateFileService {
    @Override
    public ResponseEntity<GenerateFileResponse> generateFile() {
        System.out.println("Teste 1");
        return null;
    }
}
