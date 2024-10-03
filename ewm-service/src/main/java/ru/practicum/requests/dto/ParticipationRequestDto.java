package ru.practicum.requests.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;

    private String created;

    private Long event;

    private Long requester;

    private String status;
}
