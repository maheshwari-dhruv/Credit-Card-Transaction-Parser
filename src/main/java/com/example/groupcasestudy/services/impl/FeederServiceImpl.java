package com.example.groupcasestudy.services.impl;

import com.example.groupcasestudy.modals.CreditCardData;
import com.example.groupcasestudy.services.FeederService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeederServiceImpl implements FeederService {
    List<CreditCardData> creditCardData = new ArrayList<>();

    @Override
    public List<CreditCardData> readDataFromCSV() {
        return creditCardData;
    }

    @Override
    public String uploadFile(MultipartFile file) {

        if (file == null) {
            return "Request Header - File is null";
        }

        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();

        // checking for mime type
        if (contentType != null && !contentType.matches("^text/(csv|tsv)$")) {
            return "File must be in CSV or TSV format.";
        }

        // checking the size of file
        if (size < 1024 || size > 2097152) {
            return "File size must be between 1 KiB and 2 MiB.";
        }

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBeanFilter filter = line -> {
                for (String value : line) {
                    if (value == null || value.trim().isEmpty()) {
                        return false;
                    }
                }
                return true;
            };

            CsvToBean<CreditCardData> csvToBean = new CsvToBeanBuilder<CreditCardData>(reader)
                    .withType(CreditCardData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withFilter(filter)
                    .build();

            creditCardData = csvToBean.parse();

            List<CsvException> errors = csvToBean.getCapturedExceptions();
            if (!errors.isEmpty()) {
                // handle errors here
                for (Exception e : errors) {
                    System.err.println("Error parsing CSV: " + e.getMessage());
                }
            }

            System.out.println(creditCardData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return originalFilename + " " + contentType + " " + size;
    }
}
