package ru.practicum.events.model;

public enum State {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static State getStateFromString(String stringState) {
        for (State state : State.values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + stringState);
    }
}
