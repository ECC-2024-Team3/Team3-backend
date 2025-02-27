package io.github.ecc2024team3.oimarket.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 잘못된 입력 값 처리 (유효성 검사 실패)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        String errorDetails = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.warn("🚨 [유효성 검사 오류] {}", errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("🚨 요청 데이터 유효성 검증 실패: " + errorDetails);
    }

    // ✅ 요청 파라미터 누락 예외 처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameterException(MissingServletRequestParameterException e) {
        log.warn("🚨 [필수 요청 파라미터 누락] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("🚨 필수 요청 파라미터가 누락되었습니다: " + e.getParameterName());
    }

    // ✅ 잘못된 요청 방식 예외 처리 (예: GET 대신 POST 요청 등)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("🚨 [잘못된 HTTP 요청 메서드] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("🚨 지원되지 않는 HTTP 메서드입니다: " + e.getMethod());
    }

    // ✅ 지원되지 않는 Content-Type 예외 처리
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.warn("🚨 [지원되지 않는 Content-Type] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body("🚨 지원되지 않는 Content-Type입니다: " + e.getContentType());
    }

    // ✅ ENUM 변환 오류 (잘못된 문자열 값이 ENUM으로 변환될 경우)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("🚨 [잘못된 ENUM 또는 입력값] {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("🚨 잘못된 요청 값입니다: " + e.getMessage());
    }

    // ✅ NullPointerException 예외 처리 (예상치 못한 Null 값)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        log.error("🚨 [NullPointerException 발생] {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("🚨 서버 내부 오류 (NULL 값 참조 오류): " + e.getMessage());
    }

    // ✅ 기본적인 서버 내부 오류 처리 (모든 예외)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("🚨 [서버 내부 오류 발생] {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("🚨 서버 내부 오류 발생: " + e.getMessage() + " (" + LocalDateTime.now() + ")");
    }
}
