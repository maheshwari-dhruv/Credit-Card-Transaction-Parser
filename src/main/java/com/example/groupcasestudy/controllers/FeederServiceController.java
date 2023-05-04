package com.example.groupcasestudy.controllers;

import com.example.groupcasestudy.modals.CreditCardData;
import com.example.groupcasestudy.modals.requests.ReadDataRequest;
import com.example.groupcasestudy.services.FeederService;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class FeederServiceController {

    private final FeederService feederService;

    @GetMapping("/read-all-data")
    public List<CreditCardData> readAllData(@RequestBody ReadDataRequest readDataRequest) {
        return feederService.readData(readDataRequest);
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam(value = "file") @Nullable MultipartFile file) {
        return feederService.uploadFile(file);
    }
}
