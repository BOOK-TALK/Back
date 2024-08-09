package com.book.backend.domain.detail.controller;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.detail.service.DetailService;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.DetailResponseDto;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Slf4j
public class DetailController {
    private final DetailService detailService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    // 도서 상세 (8) API
    @Operation(summary="책 상세", description="특정 책 코드를 입력으로 받아 해당 책 상세 정보, 대출 주 연령대, 키워드, 같이 대출한 도서, 추천 도서를 반환합니다.",
            parameters = {@Parameter(name = "isbn", description = "책 코드")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = DetailResponseDto.class)),
                    description = DetailResponseDto.description)})
    @GetMapping("/detail")
    public ResponseEntity<?> search(@RequestParam String isbn, String libCode) throws Exception {
        RequestLogger.param(new String[]{"isbn, libCode"}, isbn, libCode);
        requestValidate.isValidIsbn(isbn);
        requestValidate.isValidLibCode(libCode);

        DetailRequestDto requestDto = DetailRequestDto.builder().isbn13(isbn).build();
        DetailResponseDto response = detailService.detail(requestDto);

        detailService.setLoanAvailable(response, libCode);

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
