package ru.practicum.explorewithme.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.explorewithme.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class CompilationDto {

    private Long id;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
    private List<EventShortDto> events;
}