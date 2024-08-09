package com.book.backend.domain.search.controller;

import com.book.backend.domain.openapi.service.RequestValidate;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.search.dto.RequestDto;
import com.book.backend.domain.search.service.SearchService;
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
import org.springframework.web.util.UriUtils;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    private final SearchService searchService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    // 도서검색(16) API
    @Operation(summary="책 검색", description="검색어, 도서관 코드를 입력으로 받아 검색된 책 목록을 반환합니다.",
            parameters = {@Parameter(name = "isKeyword", description = "키워드 여부"),
                    @Parameter(name = "input", description = "검색어"),
                    @Parameter(name = "libCode", description = "도서관 코드"),
                    @Parameter(name = "pageNo", description = "페이지 번호"),
                    @Parameter(name = "pageSize", description = "페이지 당 요소 수")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SearchResponseDto.class)),
                    description = SearchResponseDto.description)})
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam boolean isKeyword, String input, String libCode, int pageNo, int pageSize) throws Exception {
        RequestLogger.param(new String[]{"isKeyword, input, libCode, pageNo, pageSize"}, isKeyword, input, libCode, pageNo, pageSize);
        requestValidate.isValidLibCode(libCode);

        RequestDto requestDto = RequestDto.builder()
                .isKeyword(isKeyword)
                .input(UriUtils.encode("\"" + input + "\"", "UTF-8")) // 공백 및 한글 인코딩
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();
        LinkedList<SearchResponseDto> response = searchService.search(requestDto);

        response = searchService.duplicateChecker(response);

        return responseTemplate.success(response, HttpStatus.OK);
    }
}
