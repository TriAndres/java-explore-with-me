package ru.practicum.main.exceptions;

public class CategoryIsNotEmptyException extends RuntimeException {
    public CategoryIsNotEmptyException(String message) {
        super(message);
    }
}
