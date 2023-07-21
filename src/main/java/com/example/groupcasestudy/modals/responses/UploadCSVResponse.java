package com.example.groupcasestudy.modals.responses;

import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCSVResponse {
    private BasicResponse basicResponse;
    private String fileName;
    private String fileType;
    private long fileSize;
    private List<CsvException> exceptions;
}
