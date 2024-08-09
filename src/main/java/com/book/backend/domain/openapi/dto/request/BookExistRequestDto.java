package com.book.backend.domain.openapi.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookExistRequestDto implements OpenAPIRequestInterface{
    private String libCode;
    private String isbn13;
}
