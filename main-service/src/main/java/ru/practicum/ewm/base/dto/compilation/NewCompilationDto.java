package ru.practicum.ewm.base.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {

    private Set<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(max = 128)
    private String title;
}