package com.book.backend.domain.openapi.service;

import com.book.backend.domain.openapi.dto.request.LoanItemSrchRequestDto;
import com.book.backend.domain.openapi.entity.AgeRangeCode;
import com.book.backend.domain.openapi.entity.RegionCode;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestValidate {

    public void isValidIsbn(String isbn) {
        // 숫자로 구성된 13자리 ISBN
        if(!(isbn.matches("^[0-9]*$") && isbn.length() == 13)) {
            throw new CustomException(ErrorCode.INVALID_ISBN);
        }
    }

    public LoanItemSrchRequestDto set_validLoanItemSrchRequest (String weekMonth, String peerAge, String ageRange, String gender,
                                                                  String genreCode, String regionCode, String libCode){
        LoanItemSrchRequestDto request = new LoanItemSrchRequestDto();

        if(weekMonth != null) {
            isValidWeekMonth(weekMonth);
            LocalDate today = LocalDate.now();
            if(weekMonth.equals("week")) {
                request.setStartDt(today.minusDays(7).toString());
                request.setEndDt(today.toString());
            } else {
                request.setStartDt(today.minusMonths(1).toString());
                request.setEndDt(today.toString());
            }
        }
        if(peerAge != null) {
            isValidAge(peerAge);
            request.setFrom_age(Integer.toString(Integer.parseInt(peerAge) - 2));
            request.setTo_age(Integer.toString(Integer.parseInt(peerAge) + 2));
        }
        if(ageRange != null) isValidAgeRange(ageRange);
        if(gender != null) {
            isValidGender(gender);
            if(gender.equals("man")) request.setGender("0");
            else request.setGender("1");
        }
        if(genreCode != null) isValidGenreCode(genreCode);
        if(regionCode != null) isValidRegionCode(regionCode);
        if(libCode != null) isValidLibCode(libCode);

        return request;
    }

    public void isValidWeekMonth(String weekMonth){
        if(!(weekMonth.equals("week") || weekMonth.equals("month"))){
            throw new CustomException(ErrorCode.INVALID_WEEK_MONTH);
        }
    }

    public void isValidAge(String age){
        try{ // 0~100 사이의 숫자인지 확인
            int intAge = Integer.parseInt(age);
            if(intAge < 0 || intAge > 100) throw new CustomException(ErrorCode.INVALID_AGE);
        } catch (NumberFormatException e){ // 숫자가 아님
            throw new CustomException(ErrorCode.INVALID_AGE);
        }
    }

    public void isValidAgeRange(String ageRange){
        // AgeRangeCode 코드 중 하나인지 확인
        AgeRangeCode[] ageRangeCodes = AgeRangeCode.values();
        for(AgeRangeCode code : ageRangeCodes) {
            if (code.getCode().equals(ageRange)) {
                return;
            }
        }
        throw new CustomException(ErrorCode.INVALID_AGE_RANGE);
    }

    public void isValidGender(String gender){
        // 남성, 여성 중 하나인지 확인
        if(!(gender.equals("man") || gender.equals("woman"))){
            throw new CustomException(ErrorCode.INVALID_GENDER);
        }
    }

    public void isValidGenreCode(String genreCode){
        // 두자리 숫자인지 확인
        if(!genreCode.matches("^[0-9][0-9]$")){
            throw new CustomException(ErrorCode.INVALID_GENRE_CODE);
        }
    }

    public void isValidRegionCode(String region){
        // 대지역 코드 중 하나인지 확인
        RegionCode[] regionCodes = RegionCode.values();
        for(RegionCode code : regionCodes){
            if(code.getCode().equals(region)){
                return;
            }
        }
        throw new CustomException(ErrorCode.INVALID_REGION_CODE);
    }

    public void isValidLibCode(String libCode){
        // 6자리 숫자인지 확인
        if(!(libCode.matches("^[0-9][0-9][0-9][0-9][0-9][0-9]$"))){
            throw new CustomException(ErrorCode.INVALID_LIB_CODE);
        }
    }

    // pageNo, pageSize
    public void isValidPageNum(int num) {
        // 1 이상의 숫자인지 확인
        if(num < 1) {
            throw new CustomException(ErrorCode.INVALID_PAGE_NUM);
        }
    }

//    public void isValidSearchDt(String searchDt) {
//        //yyyy-mm-dd 형식인지 확인
//        if(!searchDt.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
//            throw new CustomException(ErrorCode.INVALID_SEARCH_DT_FORMAT);
//        }
//    }
}
