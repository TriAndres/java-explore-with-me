package ru.practicum.exception;

public class ViolationException extends RuntimeException {
    public ViolationException(String message) {
        super(message);
    }
}
