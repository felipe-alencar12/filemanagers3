package com.studyings3.filemanagers3.serviceImpl;

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

import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GenerateFileServiceImpl implements GenerateFileService {

    private final RestTemplate restTemplate;

    @Value("${poke.api.url}")
    private String url;
    @Override
    public ResponseEntity<GenerateFileResponse> generateFile() {
        System.out.println("Teste 1");

        ResponseEntity<PokemonData> response = getPokemonData();
        var pokeData = response.getBody();

        generateExcelFile(pokeData);

        return null;
    }

    public void generateExcelFile(PokemonData pokemonData){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pokemon Data");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");

        Row dataRow = sheet.createRow(1);

        dataRow.createCell(0).setCellValue(pokemonData.id);
        dataRow.createCell(1).setCellValue(pokemonData.name);

        try (FileOutputStream fileOut = new FileOutputStream("pokemon_data.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Excel file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
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
