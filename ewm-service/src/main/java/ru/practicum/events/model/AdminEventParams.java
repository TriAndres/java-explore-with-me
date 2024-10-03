package ru.practicum.events.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AdminEventParams {

    private List<Long> userIds;

    private List<State> states;

    private List<Long> categoriesIds;

    private LocalDateTime start;

    private LocalDateTime end;

}
