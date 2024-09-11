package ru.practicum.ewm;

import java.time.format.DateTimeFormatter;

public class StatsConstants {
    public static final String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATA_PATTERN);
    public static final String API_HIT_PREFIX = "/hit";
    public static final String API_STATS_PREFIX = "/stats";
}