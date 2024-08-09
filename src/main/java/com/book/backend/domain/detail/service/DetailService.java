package com.book.backend.domain.detail.service;

import com.book.backend.domain.openapi.dto.request.BookExistRequestDto;
import com.book.backend.domain.openapi.dto.request.DetailRequestDto;
import com.book.backend.domain.openapi.dto.response.BookExistResponseDto;
import com.book.backend.domain.openapi.dto.response.DetailResponseDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import java.util.LinkedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DetailService {
    private final OpenAPI openAPI;

    public DetailResponseDto detail(DetailRequestDto requestDto) throws Exception {
        log.trace("detail()");
        String subUrl = "usageAnalysisList";
        JSONObject jsonResponse = openAPI.connect(subUrl, requestDto, new SearchResponseDto());
        ResponseParser responseParser = new ResponseParser();
        return responseParser.detail(jsonResponse);
    }

    public void setLoanAvailable(DetailResponseDto response, String libCode) throws Exception {
        log.trace("setLoanAvailable()");

        BookExistRequestDto bookExistRequestDto = BookExistRequestDto.builder()
                .libCode(libCode)
                .isbn13(response.getBookInfoDto().getIsbn13()).build();
        String subUrl = "bookExist";
        JSONObject jsonResponse = openAPI.connect(subUrl, bookExistRequestDto, new BookExistResponseDto());
        ResponseParser responseParser = new ResponseParser();

        response.setLoanAvailable(responseParser.loanAvailable(jsonResponse));
    }
}
