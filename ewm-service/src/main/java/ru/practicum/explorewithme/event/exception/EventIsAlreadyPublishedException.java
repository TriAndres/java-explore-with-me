package ru.practicum.explorewithme.event.exception;

public class EventIsAlreadyPublishedException extends RuntimeException {
    public EventIsAlreadyPublishedException(String message) {
        super(message);
    }
}
