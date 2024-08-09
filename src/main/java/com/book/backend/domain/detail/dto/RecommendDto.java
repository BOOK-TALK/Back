package com.book.backend.domain.detail.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendDto {
    // 마니아, 다독자
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String vol;
}
