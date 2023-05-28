package com.example.groupcasestudy.modals.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadDataRequest {
    private Boolean showAllData;
    private PaginatedRequest paginatedRequest;
    private SortDataRequest sortDataRequest;
    private FilterDataRequest filterDataRequest;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaginatedRequest {
        private int page;
        private int size;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortDataRequest {
        private String sortBy;
        private String sortDirection;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilterDataRequest {
        private String filterBy;
        private String city;
        private Double startingAmount;
        private Double endingAmount;
        private String currencyCode;
        private String cardType;
        private String transactionStatus;
    }
}
