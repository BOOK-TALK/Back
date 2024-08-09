package com.book.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ISBN(HttpStatus.BAD_REQUEST, "400", "숫자로만 구성된 13자리 ISBN 을 입력해주세요."),
    INVALID_SEARCH_DT_FORMAT(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 검색일자입니다. (yyyy-mm-dd) 형식을 맞춰주세요."),
    INVALID_MAIN_KDC(HttpStatus.BAD_REQUEST, "400", "1자리 KDC 번호를 입력해주세요."),
    INVALID_SUB_KDC(HttpStatus.BAD_REQUEST, "400", "2자리 KDC 번호를 입력해주세요."),
    INVALID_SEARCH_DT_DATE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 검색일자입니다. (어제까지 데이터 조회 가능)"),
    INVALID_WEEK_MONTH(HttpStatus.BAD_REQUEST, "400", "week 또는 month 로 입력해주세요."),
    INVALID_AGE(HttpStatus.BAD_REQUEST, "400", "0~100 사이의 숫자로 입력해주세요."),
    INVALID_AGE_RANGE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 연령 코드입니다."),
    INVALID_GENDER(HttpStatus.BAD_REQUEST, "400", "MAN 또는 WOMAN 으로 입력해주세요."),
    INVALID_GENRE_CODE(HttpStatus.BAD_REQUEST, "400", "두 자리 세부장르 코드를 입력해주세요."),
    INVALID_REGION_CODE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 지역 코드입니다."),
    INVALID_LIB_CODE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 도서관 코드입니다. (6자리 숫자로 입력해주세요)"),
    INVALID_PAGE_NUM(HttpStatus.BAD_REQUEST, "400", "페이지 번호는 1 이상의 숫자로 입력해주세요."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "401", "인증 오류가 발생했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당하는 사용자를 찾을 수 없습니다."),
    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT,"409", "사용자의 아이디가 중복됩니다."),

    API_CALL_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "429", "OPEN API 일일 호출 횟수를 초과했습니다. (일 최대 500건)"),
    ;



    private final HttpStatus status;
    private final String code;
    private final String message;
}
