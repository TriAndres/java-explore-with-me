package ru.practicum.ewm.stats;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsDTO {
    private String app;
    private String uri;
    private Long hits;
}
