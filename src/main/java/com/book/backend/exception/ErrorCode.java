package com.book.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // book
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
    INVALID_REGION_CODE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 대지역 코드입니다."),
    INVALID_REGION_DETAIL_CODE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 세부지역 코드입니다."),
    INVALID_LIB_CODE(HttpStatus.BAD_REQUEST, "400", "올바르지 않은 도서관 코드입니다. (6자리 숫자로 입력해주세요)"),
    INVALID_PAGE_NUM(HttpStatus.BAD_REQUEST, "400", "페이지 번호는 1 이상의 숫자로 입력해주세요."),
    LIST_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "400", "요청 리스트의 크기가 초과되었습니다."),
    ALREADY_EXIST(HttpStatus.CONFLICT, "409", "이미 추가된 값입니다"),

    // user
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "401", "사용자 인증에 실패했습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "401", "로그인이 필요합니다."),
    JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "401", "JWT 토큰이 입력되지 않았습니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "401", "JWT 토큰이 만료되었습니다."),
    JWT_IS_BLACKLISTED(HttpStatus.UNAUTHORIZED, "401", "더 이상 사용할 수 없는 토큰입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당하는 사용자를 찾을 수 없습니다."),
    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT,"409", "사용자의 아이디가 중복됩니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT,"409", "사용자의 username이 중복됩니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT,"409", "사용자의 닉네임이 중복됩니다."),
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "401", "JWT 토큰이 잘못되었습니다."),
    NOT_EXIST_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "404", "해당하는 Refresh token이 없습니다."),

    // goal
    INVALID_BOOK_PAGE_NUM(HttpStatus.BAD_REQUEST, "400", "책 페이지 수는 1 이상의 숫자로 입력해주세요."),
    INVALID_RECENT_PAGE(HttpStatus.BAD_REQUEST, "400", "마지막 기록보다 이전 페이지를 입력할 수 없습니다."),
    EXCEED_TOTAL_PAGE(HttpStatus.BAD_REQUEST, "400", "총 페이지를 초과하여 기록할 수 없습니다."),
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당하는 목표를 찾을 수 없습니다."),
    CANNOT_ACCESS_GOAL(HttpStatus.FORBIDDEN, "403", "해당 목표에 접근할 권한이 없습니다."),
    GOAL_IS_ALREADY_EXIST(HttpStatus.CONFLICT, "409", "해당 책에 대한 목표가 이미 존재합니다."),
    GOAL_IS_ALREADY_FINISHED(HttpStatus.BAD_REQUEST, "400", "이미 완료된 목표입니다."),

    // opentalk, message
    OPENTALK_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당하는 오픈톡을 찾을 수 없습니다.([오픈톡 참여하기]로 생성해주세요)"),
    MESSAGE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "500", "메시지 저장에 실패했습니다."),
    USER_OPENTALK_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 오픈톡은 유저의 즐겨찾기 리스트에 없습니다."),
    INVALID_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "400", "text, image, goal 중 하나를 입력해주세요."),

    // 외부 API 에러
    KAKAO_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "카카오 서버에 오류가 발생했습니다."),
    INVALID_OPENAPI_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "500", "OPEN API 서버에서 잘못된 응답을 전송했습니다."),
    API_CALL_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "429", "OPEN API 일일 호출 횟수를 초과했습니다. (일 최대 500건)"),
    LIBCODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "존재하는 도서관 코드인지 확인해주세요."),
    OPENAPI_REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "408", "OPEN API 응답을 요청하는 중 타임아웃이 발생했습니다."),

    // OAuth2
    HEADER_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Header 파싱 중 에러가 발생했습니다."),
    WRONG_PROVIDER(HttpStatus.INTERNAL_SERVER_ERROR, "500", "잘못된 인증 제공자입니다."),
    EXTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "외부 서버에 오류가 발생했습니다."),
    ID_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "400", "idToken이 입력되지 않았습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "요청이 잘못되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
