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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public ResponseEntity<GenerateFileResponse> generateFile() {
        System.out.println("Teste 1");

        ResponseEntity<PokemonData> response = getPokemonData();
        var pokeData = response.getBody();

        byte[] excelData = generateExcelFile(pokeData);

        uploadFileToS3(excelData);

        return null;
    }

    public void uploadFileToS3(byte[] fileData) {
        if (fileData != null) {
            try {
                String fileName = "pokemon_data.xlsx";
                InputStream inputStream = new java.io.ByteArrayInputStream(fileData);

                // Upload the file to S3
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, null));

                System.out.println("File uploaded successfully to S3!");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public ResponseEntity<PokemonData> getPokemonData(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                url+"/pokemon/ditto",
                HttpMethod.GET,
                entity,
                PokemonData.class
        );
    }
}
