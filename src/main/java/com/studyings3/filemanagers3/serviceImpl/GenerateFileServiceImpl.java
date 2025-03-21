package com.studyings3.filemanagers3.serviceImpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.studyings3.filemanagers3.model.response.GenerateFileResponse;
import com.studyings3.filemanagers3.model.response.PokemonData;
import com.studyings3.filemanagers3.service.GenerateFileService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;

@Service
@RequiredArgsConstructor
public class GenerateFileServiceImpl implements GenerateFileService {

    private final RestTemplate restTemplate;

    private final AmazonS3 amazonS3;

    @Value("${poke.api.url}")
    private String url;

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Override
    public ResponseEntity<GenerateFileResponse> generateFile(String pokemonName) {
        System.out.println("Teste 1");

        ResponseEntity<PokemonData> response = getPokemonData(pokemonName);
        var pokeData = response.getBody();

        byte[] excelData = generateExcelFile(pokeData);

        return uploadFileToS3(excelData);
    }

    public ResponseEntity<GenerateFileResponse> uploadFileToS3(byte[] fileData) {
        if (fileData == null) {
            return ResponseEntity.badRequest().body(new GenerateFileResponse("Failed", "No file data", null));
        }

        try {
            String fileName = "pokemon_data.xlsx";
            InputStream inputStream = new ByteArrayInputStream(fileData);

            // Upload the file to S3
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, null));

            System.out.println("File uploaded successfully to S3!");

            // Build response
            GenerateFileResponse generateFileResponse = new GenerateFileResponse();
            generateFileResponse.setStatus("Uploaded");
            generateFileResponse.setFileName(fileName);
            generateFileResponse.setFileKey(fileName);

            return ResponseEntity.ok(generateFileResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new GenerateFileResponse("Failed", "Error uploading file", null));
        }
    }


    public byte[] generateExcelFile(PokemonData pokemonData){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pokemon Data");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");

        Row dataRow = sheet.createRow(1);

        dataRow.createCell(0).setCellValue(pokemonData.id);
        dataRow.createCell(1).setCellValue(pokemonData.name);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<PokemonData> getPokemonData(String pokemonName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PokemonData> response = restTemplate.exchange(
                    url + "/pokemon/" + pokemonName,
                    HttpMethod.GET,
                    entity,
                    PokemonData.class
            );

            // Check if the response status is OK (200) and body is not null
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response;
            } else {
                System.err.println("Error: Received non-success status code " + response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (Exception e) {
            System.err.println("Error fetching Pokémon data: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
