package com.studyings3.filemanagers3.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateFileResponse {

    private String status;
    private String fileName;
    private String fileKey;

}
