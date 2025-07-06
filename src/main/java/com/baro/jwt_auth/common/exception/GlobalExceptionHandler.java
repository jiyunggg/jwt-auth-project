package com.baro.jwt_auth.common.exception;

import com.baro.jwt_auth.common.response.ErrorCode;
import com.baro.jwt_auth.common.response.ResErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResErrorDTO> handleCustomException(CustomException e) {
        ErrorCode code = e.getErrorCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(ResErrorDTO.of(code.getCode(), code.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResErrorDTO> handleException(Exception e) {
        ErrorCode fallback = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResErrorDTO.of(fallback.getCode(), fallback.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResErrorDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCode.BAD_REQUEST.getMessage());

        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus())
                .body(ResErrorDTO.of(ErrorCode.BAD_REQUEST.getCode(), errorMessage));
    }

}
