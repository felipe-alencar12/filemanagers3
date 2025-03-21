package com.studyings3.filemanagers3.service;

import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface GenerateFileService {

    ResponseEntity<GenerateFileResponse> generateFile(String pokemonName);
    
}
