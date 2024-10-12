package ru.practicum.explorewithme.request.exception;

public class EventIsNotPublishedException extends RuntimeException {
    public EventIsNotPublishedException(String message) {
        super(message);
    }
}
