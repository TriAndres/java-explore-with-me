package ru.practicum.server.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatHits {
    private String app;
    private String uri;
    private long hits;
}
