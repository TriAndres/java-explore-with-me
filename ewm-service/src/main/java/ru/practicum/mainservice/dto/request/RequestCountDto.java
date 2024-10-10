package ru.practicum.mainservice.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCountDto {
    private Long id;
    private long count;
}