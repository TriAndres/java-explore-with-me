package ru.practicum.ewm.stats;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatsDto {
    private String app;
    private String uri;
    private Long hits;
}
