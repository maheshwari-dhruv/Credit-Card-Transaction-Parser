package com.example.groupcasestudy.services;

import com.example.groupcasestudy.modals.CreditCardData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeederService {
    List<CreditCardData> readDataFromCSV();

    String uploadFile(MultipartFile file);
}
