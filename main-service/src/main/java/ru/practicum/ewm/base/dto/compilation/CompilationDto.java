package ru.practicum.ewm.base.dto.compilation;

import lombok.*;
import ru.practicum.ewm.base.dto.event.EventShortDto;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Set<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
