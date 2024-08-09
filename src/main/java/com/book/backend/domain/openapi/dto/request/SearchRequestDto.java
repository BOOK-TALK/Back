package com.book.backend.domain.openapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class SearchRequestDto implements OpenAPIRequestInterface {
    @NotBlank(message = "검색어를 입력해주세요.")
    private String title;
    private String author;
    private String keyword;
    private String pageNo; // 페이지 번호
    private String pageSize; // 한 페이지에 담을 요소 수
}
