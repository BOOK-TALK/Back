package com.book.backend.global.log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Dto 객체의 필드와 값을 로깅 (request logging)
public class RequestLogger {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);

    public static void param(String[] keys, Object ... params) {
        StringBuilder sb = new StringBuilder();
        Iterator<Object> paramIter = Arrays.stream(params).iterator();

        sb.append("RequestParam : {");
        for(String key : keys){
            if(paramIter.hasNext()){
                Object val = paramIter.next();
                if(val != null){
                    String valueType = val.getClass().getSimpleName();
                    sb.append(key).append(": ").append(valueType);
                    if(paramIter.hasNext()) sb.append(", ");
                }
            }
        }
        sb.append("}");

        logger.trace(sb.toString());
    }

    public static void body(Object dto) {
        StringBuilder logMessage = new StringBuilder();
        Class<?> clazz = dto.getClass();
        String dtoName = dto.getClass().getSimpleName();
        Field[] fields = clazz.getDeclaredFields(); // 모든 필드를 가져옴
        // log 커스텀
        logMessage.append("RequestBody : ");
        logMessage.append(dtoName).append(" {");
        for (Field field : fields) {
            field.setAccessible(true); // private 필드 접근 허용
            String key = field.getName();
            String valueType = field.getType().getSimpleName();
            logMessage.append(key).append(": ").append(valueType).append(", ");
        }
        // 마지막 콤마 제거
        if (logMessage.length() > 2) {
            logMessage.setLength(logMessage.length() - 2);
        }
        logMessage.append("}");

        logger.trace(logMessage.toString());
    }
}
