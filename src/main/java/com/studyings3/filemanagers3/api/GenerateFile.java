package com.studyings3.filemanagers3.api;

import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface GenerateFile {

    ResponseEntity<GenerateFileResponse> generateFile(@RequestParam String pokemonName);

}
