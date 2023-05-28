package com.example.groupcasestudy.validations;

import org.springframework.web.multipart.MultipartFile;

public class UploadCSVFileServiceValidation {

    public static boolean checkForFilterByAndSortBy(String filterBy, String sortBy) {
        return (filterBy == null || filterBy.isEmpty()) && (sortBy == null || sortBy.isEmpty());
    }


    public static boolean checkFileIsNull(MultipartFile file) {
        return file == null;
    }

    public static boolean checkFileMIMEType(String contentType) {
        return contentType != null && !contentType.matches("^text/(csv|tsv)$");
    }

    public static boolean checkFileSize(long size) {
        return size < 1024 || size > 2097152;
    }
}
