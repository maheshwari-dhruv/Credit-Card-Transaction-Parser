package com.example.groupcasestudy.controllers;

import com.example.groupcasestudy.modals.CreditCardData;
import com.example.groupcasestudy.services.FeederService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class FeederServiceController {

    private final FeederService feederService;

    @GetMapping("/read-csv")
    public List<CreditCardData> readCSVData() {
        return feederService.readDataFromCSV();
    }
}
