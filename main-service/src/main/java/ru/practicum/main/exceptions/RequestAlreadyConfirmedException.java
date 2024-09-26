package ru.practicum.main.exceptions;

public class RequestAlreadyConfirmedException extends RuntimeException {
    public RequestAlreadyConfirmedException(String message) {
        super(message);
    }
}
