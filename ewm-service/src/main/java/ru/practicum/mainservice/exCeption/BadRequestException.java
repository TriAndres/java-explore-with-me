package ru.practicum.mainservice.exCeption;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}