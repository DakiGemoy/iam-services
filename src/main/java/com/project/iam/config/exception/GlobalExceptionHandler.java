package com.project.iam.config.exception;

import com.project.iam.models.response.BaseResponse;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<?,?>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
                BaseResponse.builder()
                        .code("500")
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<BaseResponse<?,?>> handleCustomException(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException mex){
            mex.getBindingResult().getFieldErrors()
                    .forEach(e -> errors.put(e.getField(), "Can't be empty"));
        } else {
            ((BindException) ex).getBindingResult().getFieldErrors()
                    .forEach(e -> errors.put(e.getField(), "Can't be empty"));
        }

        return ResponseEntity.badRequest()
                .body(
                        BaseResponse.builder()
                                .message("Bad Request")
                                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                                .exception(errors)
                                .build()
                );
    }

    @ExceptionHandler(ProcessException.class)
    public ResponseEntity<BaseResponse<?,?>> handleCustomException(ProcessException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(
                        BaseResponse.builder()
                                .code(
                                        ex.getCode()!=null ? ex.getCode() : String.valueOf(ex.getStatus().value())
                                )
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler({
            Exception.class,
            RuntimeException.class})
    public ResponseEntity<BaseResponse<?,?>> handleBaseException(Exception ex) {
        return ResponseEntity.status(500)
                .body(
                        BaseResponse.builder()
                                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<BaseResponse<?,?>> handlerNotUniqueData(IncorrectResultSizeDataAccessException ex) {
        return ResponseEntity.status(500)
                .body(
                        BaseResponse.builder()
                                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                .message("Hasil pencarian data tidak sesuai")
                                .build()
                );
    }
}
