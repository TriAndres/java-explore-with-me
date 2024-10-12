package ru.practicum.explorewithme.comment.exception;

public class NotAuthorException extends RuntimeException {
    public NotAuthorException(String message) {
        super(message);
    }
}