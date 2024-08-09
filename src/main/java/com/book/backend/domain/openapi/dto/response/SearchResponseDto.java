package com.book.backend.domain.openapi.dto.response;

import com.book.backend.domain.openapi.dto.request.OpenAPIRequestInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class SearchResponseDto implements OpenAPIResponseInterface, OpenAPIRequestInterface {
    private String bookname;
    private String authors;
    private String publisher;
    private String publication_year;
    private String isbn13;
    private String vol;
    private String bookImageURL;
    private String bookDtlUrl;
    private String loan_count;

    private boolean isLoanAvailable;

    public static final String description = "bookname: 책이름 | authors: 작가 옮긴이 | publisher: 출판사 | "
            + "publication_year: 출판년도 | isbn13: 책 바코드 | vol: 권? | "
            + "bookImageURL: 책 표지 URL | bookDtlUrl: 책 상세설명 URL | loan_count: 총? 대출 회수";
}
