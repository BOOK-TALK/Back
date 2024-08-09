package com.book.backend.domain.search.service;

import com.book.backend.domain.openapi.dto.request.SearchRequestDto;
import com.book.backend.domain.openapi.dto.response.SearchResponseDto;
import com.book.backend.domain.openapi.service.OpenAPI;
import com.book.backend.domain.openapi.service.ResponseParser;
import com.book.backend.domain.search.dto.RequestDto;
import java.util.HashSet;
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
public class SearchService {
    private final OpenAPI openAPI;

    public LinkedList<SearchResponseDto> search(RequestDto requestDto) throws Exception {
        log.trace("search()");
        String subUrl = "srchBooks";

        ResponseParser responseParser = new ResponseParser();
        if(!requestDto.isKeyword()){
            LinkedList<SearchResponseDto> responseList = new LinkedList<>();
            Integer halfPageSize = requestDto.getPageSize() / 2;

            SearchRequestDto searchRequestDto = SearchRequestDto.builder()
                    .pageNo(requestDto.getPageNo().toString())
                    .pageSize(halfPageSize.toString()) // 셔플할 리스트의 페이지 크기 설정
                    .build();

            // 제목으로 검색
            searchRequestDto.setTitle(requestDto.getInput());
            JSONObject jsonResponse = openAPI.connect(subUrl, searchRequestDto, new SearchResponseDto());
            responseList.addAll(responseParser.search(jsonResponse));

            // 작가로 검색
            searchRequestDto.setTitle(null);
            searchRequestDto.setAuthor(requestDto.getInput());
            jsonResponse = openAPI.connect(subUrl, searchRequestDto, new SearchResponseDto());
            responseList.addAll(responseParser.search(jsonResponse));

            return duplicateChecker(responseList);
        } else { // 키워드로 검색
            SearchRequestDto searchRequestDto = SearchRequestDto.builder()
                    .pageNo(requestDto.getPageNo().toString())
                    .pageSize(String.valueOf(requestDto.getPageSize())) // 셔플할 리스트의 페이지 크기 설정
                    .build();

            searchRequestDto.setKeyword(requestDto.getInput());
            JSONObject jsonResponse = openAPI.connect(subUrl, searchRequestDto, new SearchResponseDto());
            return responseParser.search(jsonResponse);
        }
    }

    public LinkedList<SearchResponseDto> duplicateChecker(LinkedList<SearchResponseDto> list){
        log.trace("duplicateChecker()");
        LinkedList<SearchResponseDto> duplicateRemovedList = new LinkedList<>();
        HashSet<String> set = new HashSet<>();
        for(SearchResponseDto dto : list){
            String key = dto.getBookname() + dto.getAuthors();
            if(set.add(key)) duplicateRemovedList.add(dto);
        }
        return duplicateRemovedList;
    }
}
