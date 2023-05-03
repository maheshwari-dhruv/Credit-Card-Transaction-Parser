package com.example.groupcasestudy.services.impl;

import com.example.groupcasestudy.modals.CreditCardData;
import com.example.groupcasestudy.services.FeederService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeederServiceImpl implements FeederService {

    @Override
    public List<CreditCardData> readDataFromCSV() {
        List<CreditCardData> creditCardData = new ArrayList<>();
        try {
            // create a reader
            Reader reader = Files.newBufferedReader(Path.of("data/test-data.csv"));

//            // create csv bean reader
            CsvToBean<CreditCardData> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CreditCardData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();


            creditCardData.addAll(csvToBean.parse());
            System.out.println(creditCardData);

            // close the reader
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return creditCardData;
    }
}
