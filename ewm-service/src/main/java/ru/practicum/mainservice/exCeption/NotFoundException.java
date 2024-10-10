package ru.practicum.mainservice.exCeption;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}