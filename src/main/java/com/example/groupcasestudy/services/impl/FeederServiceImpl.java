package com.example.groupcasestudy.services.impl;

import com.example.groupcasestudy.modals.CreditCardData;
import com.example.groupcasestudy.modals.errors.ErrorResponse;
import com.example.groupcasestudy.modals.requests.ReadDataRequest;
import com.example.groupcasestudy.modals.responses.BasicResponse;
import com.example.groupcasestudy.modals.responses.UploadCSVResponse;
import com.example.groupcasestudy.services.FeederService;
import com.example.groupcasestudy.validations.UploadCSVFileServiceValidation;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.exceptions.CsvException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        if (Boolean.TRUE.equals(readDataRequest.getShowAllData())) {
            return readAllData(paginatedList, readDataRequest.getSortDataRequest(), readDataRequest.getFilterDataRequest());
        }

        return readUpdatedData(paginatedList, readDataRequest.getPaginatedRequest(), readDataRequest.getSortDataRequest(), readDataRequest.getFilterDataRequest());
    }

    private List<CreditCardData> readUpdatedData(List<CreditCardData> paginatedList, ReadDataRequest.PaginatedRequest paginatedRequest, ReadDataRequest.SortDataRequest sortDataRequest, ReadDataRequest.FilterDataRequest filterDataRequest) {
        List<CreditCardData> paginatedData = paginatedList(paginatedList, paginatedRequest);
        return getCreditCardData(sortDataRequest, filterDataRequest, paginatedData);
    }

    private List<CreditCardData> readAllData(List<CreditCardData> paginatedList, ReadDataRequest.SortDataRequest sortDataRequest, ReadDataRequest.FilterDataRequest filterDataRequest) {
        return getCreditCardData(sortDataRequest, filterDataRequest, paginatedList);
    }

    private List<CreditCardData> getCreditCardData(ReadDataRequest.SortDataRequest sortDataRequest, ReadDataRequest.FilterDataRequest filterDataRequest, List<CreditCardData> paginatedData) {
        if (UploadCSVFileServiceValidation.checkForFilterByAndSortBy(filterDataRequest.getFilterBy(), sortDataRequest.getSortBy())) {
            return paginatedData;
        }

        if (sortDataRequest.getSortBy() != null && !sortDataRequest.getSortBy().isEmpty()) {
            sortList(paginatedData, sortDataRequest);
        }

        if (filterDataRequest.getFilterBy() != null && !filterDataRequest.getFilterBy().isEmpty()) {
            paginatedData = filterList(paginatedData, filterDataRequest);
        }

        return paginatedData;
    }

    private List<CreditCardData> paginatedList(List<CreditCardData> paginatedList, ReadDataRequest.PaginatedRequest paginatedRequest) {
        int startIndex = paginatedRequest.getPage() * paginatedRequest.getSize();
        int endIndex = Math.min(startIndex + paginatedRequest.getSize(), paginatedList.size());
        return paginatedList.subList(startIndex, endIndex);
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
    public ResponseEntity<Object> uploadFile(MultipartFile file) {

        // checking if file sent in header is null
        ResponseEntity<Object> nullErrorResponse = checkNull(UploadCSVFileServiceValidation.checkFileIsNull(file), HttpStatus.NOT_FOUND, "No File found");
        if (nullErrorResponse != null) return nullErrorResponse;

        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        List<CsvException> errors = new ArrayList<>();

        // checking for mime type
        ResponseEntity<Object> mimeErrorResponse = checkMIMEorSize(UploadCSVFileServiceValidation.checkFileMIMEType(contentType), HttpStatus.NOT_ACCEPTABLE, "File can only be in CSV or TSV");
        if (mimeErrorResponse != null) return mimeErrorResponse;

        // checking the size of file
        ResponseEntity<Object> sizeErrorResponse = checkMIMEorSize(UploadCSVFileServiceValidation.checkFileSize(size), HttpStatus.NOT_ACCEPTABLE, "File size must be between 1 KiB and 2 MiB.");
        if (sizeErrorResponse != null) return sizeErrorResponse;

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

            errors = csvToBean.getCapturedExceptions();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        BasicResponse basicResponse = BasicResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data fetch successfully from csv file")
                .build();

        UploadCSVResponse uploadCSVResponse = UploadCSVResponse.builder()
                .basicResponse(basicResponse)
                .fileName(originalFilename)
                .fileType(contentType)
                .fileSize(size)
                .exceptions(errors)
                .build();

        return ResponseEntity.ok(uploadCSVResponse);
    }

    private ResponseEntity<Object> checkMIMEorSize(boolean contentType, HttpStatus notAcceptable, String message) {
        if (contentType) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .statusCode(notAcceptable.value())
                    .message(message)
                    .build();

            return ResponseEntity.ok(errorResponse);
        }
        return null;
    }

    private ResponseEntity<Object> checkNull(boolean file, HttpStatus notFound, String No_File_found) {
        if (file) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .statusCode(notFound.value())
                    .message(No_File_found)
                    .build();

            return ResponseEntity.ok(errorResponse);
        }
        return null;
    }
}
