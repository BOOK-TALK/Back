package com.book.backend.domain.book.service;

import com.book.backend.domain.openapi.dto.ManiaRequestDto;
import com.book.backend.domain.openapi.dto.TestDto;
import com.book.backend.domain.openapi.controller.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    @Autowired
    private final OpenAPI openAPI;

    public void mania(ManiaRequestDto maniaDto) throws Exception {
        String subUrl = "recommandList";
        openAPI.connect(subUrl, maniaDto); //반환값
    }

    public void test(TestDto dto) throws Exception{
        String subUrl = "libSrch";
        openAPI.connect(subUrl, dto); //반환값
    }
}
