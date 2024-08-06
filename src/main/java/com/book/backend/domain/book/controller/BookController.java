package com.book.backend.domain.book.controller;

import com.book.backend.domain.book.service.BookService;
import com.book.backend.domain.book.service.RequestValidate;
import com.book.backend.domain.openapi.dto.request.CustomHotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.HotTrendRequestDto;
import com.book.backend.domain.openapi.dto.request.KeywordRequestDto;
import com.book.backend.domain.openapi.dto.response.CustomHotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.HotTrendResponseDto;
import com.book.backend.domain.openapi.dto.response.KeywordResponseDto;
import com.book.backend.domain.openapi.dto.response.RecommendResponseDto;
import com.book.backend.domain.openapi.dto.request.RecommendRequestDto;
import com.book.backend.global.ResponseTemplate;
import com.book.backend.global.log.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.LocalDate;
import java.util.HashSet;
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
public class BookController {
    private final BookService bookService;
    private final RequestValidate requestValidate;
    private final ResponseTemplate responseTemplate;

    // 마니아(4), 다독자(5) 추천 API
    @Operation(summary="책 추천", description="특정 책 코드를 입력으로 받아 해당 책 기반 추천 책 list를 반환합니다.",
            parameters = {@Parameter(name = "isbn", description = "책 코드")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RecommendResponseDto.class)),
                        description = RecommendResponseDto.description)})
    @GetMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestParam String isbn) throws Exception {
        RequestLogger.param(new String[]{"isbn"}, isbn);
        requestValidate.isValidIsbn(isbn);

        RecommendRequestDto requestDto = RecommendRequestDto.builder().isbn13(isbn).build();
        LinkedList<RecommendResponseDto> response = bookService.recommend(requestDto);

        HashSet<String> duplicateCheckSet = new HashSet<>();
        LinkedList<RecommendResponseDto> duplicateRemovedList = bookService.duplicateChecker(response, duplicateCheckSet);
        bookService.ensureRecommendationsCount(duplicateRemovedList, duplicateCheckSet);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 대출급상승(12) API
    @Operation(summary="대출 급상승", description="지난 3일간 대출 급상승 책 list 를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = HotTrendResponseDto.class)),
                        description = HotTrendResponseDto.description)})
    @GetMapping("/hotTrend")
    public ResponseEntity<?> hotTrend() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        HotTrendRequestDto requestDto = HotTrendRequestDto.builder().searchDt(yesterday.toString()).build();
        LinkedList<HotTrendResponseDto> response = bookService.hotTrend(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 지난달 키워드 (17)
    @Operation(summary="지난달 키워드", description="지난달 핵심 키워드 100개 중 랜덤으로 10개 키워드를 반환합니다.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = KeywordResponseDto.class)),
                        description = KeywordResponseDto.description)})
    @GetMapping("/keyword")
    public ResponseEntity<?> keywords() throws Exception {
        KeywordRequestDto requestDto = new KeywordRequestDto();
        requestDto.setSearchDt();

        LinkedList<KeywordResponseDto> response = bookService.keywords(requestDto);

        return responseTemplate.success(response, HttpStatus.OK);
    }

    // 조건형 인기 대출 책 (3)
    @Operation(summary="조건형 인기 대출 도서", description="조건 값에 따른 인기 대출 책 list 를 반환합니다. (모두 선택값입니다)",
            parameters = {
            @Parameter(name = "weekMonth", description = "'week' 또는 'month'"),
            @Parameter(name = "peerAge", description = "age +- 2 범위의 나이로 또래 인기 대출 책을 조회"),
            @Parameter(name = "ageRange", description = "연령대 코드 (0, 6, 8, 14, 20, 30, 40, 50, 60)"),
            @Parameter(name = "gender", description = "'man' 또는 'woman'"),
            @Parameter(name = "genreCode", description = "세부 장르 코드 (figma 참고)"),
            @Parameter(name = "region", description = "시단위 지역코드 (figma 참고)"),
            @Parameter(name = "libCode", description = "도서관 코드 (조회 -> https://www.data4library.kr/libDataL)")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CustomHotTrendResponseDto.class)),
                        description = CustomHotTrendResponseDto.description)})
    @GetMapping("/customHotTrend")
    public ResponseEntity<?> customHotTrend(@RequestParam(required = false) String weekMonth,
                                            @RequestParam(required = false) String peerAge,
                                            @RequestParam(required = false) String ageRange,
                                            @RequestParam(required = false) String gender,
                                            @RequestParam(required = false) String genreCode,
                                            @RequestParam(required = false) String region,
                                            @RequestParam(required = false) String libCode) throws Exception {
        RequestLogger.param(new String[]{"weekMonth", "peerAge", "ageRange", "gender", "genreCode", "region", "libCode"},
                weekMonth, peerAge, ageRange, gender, genreCode, region, libCode);
        CustomHotTrendRequestDto requestDto = requestValidate.set_validCustomHotTrendRequest(weekMonth, peerAge, ageRange, gender, genreCode, region, libCode);

        LinkedList<CustomHotTrendResponseDto> response = bookService.customHotTrend(requestDto);
        return responseTemplate.success(response, HttpStatus.OK);
    }
}
