package ru.practicum.mainservice.exCeption;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}