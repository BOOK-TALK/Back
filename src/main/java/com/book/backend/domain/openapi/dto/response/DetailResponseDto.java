package com.book.backend.domain.openapi.dto.response;

import com.book.backend.domain.detail.dto.BookInfoDto;
import com.book.backend.domain.detail.dto.CoLoanBooksDto;
import com.book.backend.domain.detail.dto.Top3LoanUserDto;
import com.book.backend.domain.detail.dto.RecommendDto;
import java.util.LinkedList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class DetailResponseDto implements OpenAPIResponseInterface {
    private BookInfoDto bookInfoDto;
    private List<Top3LoanUserDto> top3LoanUserDtoList;
    private LinkedList<String> keywordDtoList;
    private LinkedList<CoLoanBooksDto> coLoanBooksDtoList;
    private LinkedList<RecommendDto> recommendResponseDtoList;
    private boolean loanAvailable;

    public static final String description = "bookInfoDto:  책 상세 정보 | top3LoanUserDtoList: 대출 주 연령대 | "
            + "keywordDtoList: 책 연관 키워드 | coLoanBooksDtoList: 같이 대출한 도서 | recommendResponseDtoList: 추천 도서";
}
