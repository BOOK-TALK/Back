package com.book.backend.domain.detail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoLoanBooksDto {
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String vol;
    private String loanCnt;
}
