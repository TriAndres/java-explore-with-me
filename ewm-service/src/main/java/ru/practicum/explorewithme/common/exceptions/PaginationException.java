package ru.practicum.explorewithme.common.exceptions;

public class PaginationException extends RuntimeException {
    public PaginationException(String message) {
        super(message);
    }
}