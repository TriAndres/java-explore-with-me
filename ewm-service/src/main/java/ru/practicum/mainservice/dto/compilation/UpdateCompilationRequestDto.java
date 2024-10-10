package ru.practicum.mainservice.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UpdateCompilationRequestDto {
    @Size(min = 2, max = 50)
    private String title;
    private Set<Long> events;
    private Boolean pinned;
}