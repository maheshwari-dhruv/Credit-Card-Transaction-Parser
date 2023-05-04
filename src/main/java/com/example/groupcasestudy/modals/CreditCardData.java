package com.example.groupcasestudy.modals;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardData {
    @CsvBindByName
    private UUID transactionId;
    @CsvBindByName
    private String cardHolderName;
    @CsvBindByName
    private String creditCardNumber;
    @CsvBindByName
    private String transactionDate;
    @CsvBindByName
    private String merchantName;
    @CsvBindByName
    private String merchantCity;
    @CsvBindByName
    private String merchantPostalCode;
    @CsvBindByName
    private Double amount;
    @CsvBindByName
    private String currencyCode;
    @CsvBindByName
    private String transactionStatus;
    @CsvBindByName
    private String cardType;
}
