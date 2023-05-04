package com.example.groupcasestudy.services.impl;

import com.example.groupcasestudy.modals.CreditCardData;
import com.example.groupcasestudy.modals.requests.ReadDataRequest;
import com.example.groupcasestudy.services.FeederService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeederServiceImpl implements FeederService {
    List<CreditCardData> creditCardData = new ArrayList<>();

    @Override
    public List<CreditCardData> readData(ReadDataRequest readDataRequest) {
        List<CreditCardData> paginatedList = new ArrayList<>(creditCardData);

        if (readDataRequest.getShowAllData()) {
            return readAllData(paginatedList, readDataRequest.getSortDataRequest(), readDataRequest.getFilterDataRequest());
        }

        return readUpdatedData(paginatedList, readDataRequest.getPaginatedRequest(), readDataRequest.getSortDataRequest(), readDataRequest.getFilterDataRequest());
    }

    private List<CreditCardData> readUpdatedData(List<CreditCardData> paginatedList, ReadDataRequest.PaginatedRequest paginatedRequest, ReadDataRequest.SortDataRequest sortDataRequest, ReadDataRequest.FilterDataRequest filterDataRequest) {
        // apply pagination
        int startIndex = paginatedRequest.getPage() * paginatedRequest.getSize();
        int endIndex = Math.min(startIndex + paginatedRequest.getSize(), paginatedList.size());
        int totalPages = paginatedList.size() / paginatedRequest.getSize();
        System.out.println(totalPages);
        List<CreditCardData> paginatedData = paginatedList.subList(startIndex, endIndex);

        // apply filter & sorting if present
        if ((filterDataRequest.getFilterBy() == null || filterDataRequest.getFilterBy().isEmpty()) && (sortDataRequest.getSortBy() == null || sortDataRequest.getSortBy().isEmpty())) {
            return paginatedData;
        }

        if (filterDataRequest.getFilterBy() != null && !filterDataRequest.getFilterBy().isEmpty()) {
            paginatedData = filterList(paginatedList, filterDataRequest);
        }

        if (sortDataRequest.getSortBy() != null && !sortDataRequest.getSortBy().isEmpty()) {
            sortList(paginatedList, sortDataRequest);
        }

        return paginatedData;
    }

    private List<CreditCardData> readAllData(List<CreditCardData> paginatedList, ReadDataRequest.SortDataRequest sortDataRequest, ReadDataRequest.FilterDataRequest filterDataRequest) {
        // apply filter & sorting if present
        if ((filterDataRequest.getFilterBy() == null || filterDataRequest.getFilterBy().isEmpty()) && (sortDataRequest.getSortBy() == null || sortDataRequest.getSortBy().isEmpty())) {
            return paginatedList;
        }

        if (filterDataRequest.getFilterBy() != null && !filterDataRequest.getFilterBy().isEmpty()) {
            paginatedList = filterList(paginatedList, filterDataRequest);
        }

        if (sortDataRequest.getSortBy() != null && !sortDataRequest.getSortBy().isEmpty()) {
            sortList(paginatedList, sortDataRequest);
        }

        return paginatedList;
    }

    private List<CreditCardData> filterList(List<CreditCardData> paginatedList, ReadDataRequest.FilterDataRequest filterDataRequest) {
        List<CreditCardData> filteredList = new ArrayList<>();
        if ("MERCHANT_CITY".equalsIgnoreCase(filterDataRequest.getFilterBy())) {
            filteredList = paginatedList.stream()
                    .filter(d -> d.getMerchantCity().equalsIgnoreCase(filterDataRequest.getCity()))
                    .toList();
        } else if ("AMOUNT".equalsIgnoreCase(filterDataRequest.getFilterBy())) {
            filteredList = paginatedList.stream()
                    .filter(d -> d.getAmount() <= filterDataRequest.getEndingAmount() && d.getAmount() >= filterDataRequest.getStartingAmount())
                    .toList();
        } else if ("CURRENCY_CODE".equalsIgnoreCase(filterDataRequest.getFilterBy())) {
            filteredList = paginatedList.stream()
                    .filter(d -> d.getCurrencyCode().equalsIgnoreCase(filterDataRequest.getCurrencyCode()))
                    .toList();
        } else if ("TRANSACTION_STATUS".equalsIgnoreCase(filterDataRequest.getFilterBy())) {
            filteredList = paginatedList.stream()
                    .filter(d -> d.getTransactionStatus().equalsIgnoreCase(filterDataRequest.getTransactionStatus()))
                    .toList();
        } else if ("CARD_TYPE".equalsIgnoreCase(filterDataRequest.getFilterBy())) {
            filteredList = paginatedList.stream()
                    .filter(d -> d.getCardType().equalsIgnoreCase(filterDataRequest.getCardType()))
                    .toList();
        }

        return filteredList;
    }

    private void sortList(List<CreditCardData> paginatedList, ReadDataRequest.SortDataRequest sortDataRequest) {
        if ("AMOUNT".equalsIgnoreCase(sortDataRequest.getSortBy())) {
            paginatedList.sort((s1, s2) -> {
                int comparison = s1.getAmount().compareTo(s2.getAmount());
                return "DESC".equalsIgnoreCase(sortDataRequest.getSortDirection()) ? -comparison : comparison;
            });
        }
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
