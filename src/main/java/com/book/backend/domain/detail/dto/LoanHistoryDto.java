package com.book.backend.domain.detail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanHistoryDto {
    private String month;
    private String loanCnt;
    private String ranking;
}
