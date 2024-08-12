package com.book.backend.domain.genre.service;

import com.book.backend.domain.openapi.dto.response.LoanItemSrchResponseDto;
import com.book.backend.domain.openapi.service.RandomPicker;
import com.book.backend.domain.openapi.service.ResponseParser;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@RequiredArgsConstructor
public class GenreResponseParser {
    private final ResponseParser responseParser;
    private static final int NEW_TREND_YEAR_OFFSET = 2;  // 최근 트렌드 연도 범위

    public LinkedList<LoanItemSrchResponseDto> periodTrend(JSONObject jsonResponse) {
        return filterResponses(jsonResponse, null, null);
    }

    public LinkedList<LoanItemSrchResponseDto> random(JSONObject jsonResponse, Integer maxSize) {
        LinkedList<LoanItemSrchResponseDto> filteredResponses = filterResponses(jsonResponse, null, maxSize);
        return RandomPicker.randomPick(filteredResponses, maxSize);
    }

    public LinkedList<LoanItemSrchResponseDto> newTrend(JSONObject jsonResponse, int currentYear) {
        return filterResponses(jsonResponse, currentYear, null);
    }

    private LinkedList<LoanItemSrchResponseDto> filterResponses(JSONObject jsonResponse, Integer yearThreshold, Integer maxSize) {
        LinkedList<LoanItemSrchResponseDto> loanTrendResponseList = responseParser.loanTrend(jsonResponse);
        LinkedList<LoanItemSrchResponseDto> responseList = new LinkedList<>();

        for (LoanItemSrchResponseDto response : loanTrendResponseList) {
            if (maxSize != null && responseList.size() >= maxSize) {
                break;
            }

            if (yearThreshold != null) {
                int publicationYear;
                try {
                    publicationYear = Integer.parseInt(response.getPublication_year());
                } catch (NumberFormatException e) {
                    continue; // 유효하지 않은 년도 데이터는 무시
                }

                if (publicationYear >= yearThreshold - NEW_TREND_YEAR_OFFSET) {
                    responseList.add(response);
                }
            } else {
                responseList.add(response);
            }
        }
        return responseList;
    }
}