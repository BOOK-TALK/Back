package com.book.backend.domain.detail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookInfoDto {
    private String bookname;
    private String authors;
    private String publisher;
    private String bookImageURL;
    private String description;
    private String publication_year;
    private String isbn13;
    private String vol;
    private String class_no;
    private String class_nm;
    private String loanCnt;
}
