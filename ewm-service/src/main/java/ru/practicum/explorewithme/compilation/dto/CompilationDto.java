package ru.practicum.explorewithme.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
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
    @Length(max = 50)
    private String title;
    private List<EventShortDto> events;
}