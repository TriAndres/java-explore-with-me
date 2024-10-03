package ru.practicum.events.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CountDto {

    private Long eventId;

    private Long count;
}