package ru.practicum.mainservice.exCeption;

public class StatServerConnectException extends RuntimeException {
    public StatServerConnectException(String message) {
        super(message);
    }
}