package ru.practicum.mainservice.dto.compilation;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.practicum.mainservice.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {
    @Positive
    private int id;
    @NotBlank
    private String title;
    private List<EventShortDto> events;
    private boolean pinned;
}