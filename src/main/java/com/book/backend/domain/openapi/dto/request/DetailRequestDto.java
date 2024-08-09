package com.book.backend.domain.openapi.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailRequestDto implements OpenAPIRequestInterface {
    private String isbn13;
}
