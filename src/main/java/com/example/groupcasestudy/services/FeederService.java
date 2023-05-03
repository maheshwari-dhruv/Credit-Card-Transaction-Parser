package com.example.groupcasestudy.services;

import com.example.groupcasestudy.modals.CreditCardData;

import java.util.List;

public interface FeederService {
    List<CreditCardData> readDataFromCSV();
}
