package com.qingfan.stocksync.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(VendorApiException.class)
    public ResponseEntity<ErrorResponse> handleVendorApiException(VendorApiException e) {
        log.error("Vendor API Error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorResponse.of("VENDOR_API_ERROR", e.getMessage()));
    }

    @ExceptionHandler(VendorFileException.class)
    public ResponseEntity<ErrorResponse> handleVendorFileException(VendorFileException e) {
        log.error("Vendor File Error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of("VENDOR_FILE_ERROR", e.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException e) {
        log.warn("Product Not Found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of("PRODUCT_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Internal Error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of("INTERNAL_ERROR", "internal error"));
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    static class ErrorResponse {
        private String code;
        private String message;
        private final Instant timestamp = Instant.now();
    }
}
