package com.studyings3.filemanagers3.api;

import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import org.springframework.http.ResponseEntity;

public interface GenerateFile {

    ResponseEntity<GenerateFileResponse> generateFile();

}
