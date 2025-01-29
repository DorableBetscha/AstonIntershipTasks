package com.springmvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //обработка исключений в контроллерах
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class) //будет вызван когда будет выброшено исключение
    public ResponseEntity<String> handleNotFoundException (NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()); //возвращает HTTP-ответ, статус 404
    }

    @ExceptionHandler(Exception.class) //будет выброшен для всех остальных исключений, ответ с кодом 500
    public ResponseEntity<String> handleGeneralException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
