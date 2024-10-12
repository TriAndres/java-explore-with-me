package ru.practicum.explorewithme.event.exception;

public class EventIsTooSoonException extends RuntimeException {
    public EventIsTooSoonException(String message) {
        super(message);
    }
}
