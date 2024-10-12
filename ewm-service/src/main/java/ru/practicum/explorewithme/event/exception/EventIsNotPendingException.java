package ru.practicum.explorewithme.event.exception;

public class EventIsNotPendingException extends RuntimeException {
    public EventIsNotPendingException(String message) {
        super(message);
    }
}
