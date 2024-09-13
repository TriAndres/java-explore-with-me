package ru.practicum.base.enams;

public enum Status {
    PENDING;

    public static Status from(String status) {
        for (Status value : Status.values()) {
            if (value.name().equalsIgnoreCase(status)) {
                return value;
            }
        }
        return null;
    }
}
