package com.springmvc.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message); //передает message в конструктор родительского класса - стандартные механизмы обработки
    }
}
