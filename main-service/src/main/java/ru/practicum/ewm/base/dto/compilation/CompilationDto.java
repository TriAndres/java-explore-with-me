package ru.practicum.ewm.base.dto.compilation;

import lombok.*;
import ru.practicum.ewm.base.dto.event.EventShortDto;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {

    private Set<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
