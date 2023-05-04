package com.example.groupcasestudy.services;

import com.example.groupcasestudy.modals.CreditCardData;
import com.example.groupcasestudy.modals.requests.ReadDataRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeederService {
    String uploadFile(MultipartFile file);
    List<CreditCardData> readData(ReadDataRequest readDataRequest);
}
