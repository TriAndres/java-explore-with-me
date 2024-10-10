package ru.practicum.model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatResponseDto {
    private String app;
    private String uri;
    private Long hits;
}