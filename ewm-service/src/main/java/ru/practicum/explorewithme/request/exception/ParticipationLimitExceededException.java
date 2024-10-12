package ru.practicum.explorewithme.request.exception;

public class ParticipationLimitExceededException extends RuntimeException {
    public ParticipationLimitExceededException(String message) {
        super(message);
    }
}
