package ru.practicum.compilations.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUpdateDto {

    private List<Long> events = List.of();

    private Boolean pinned;

    @Size(max = 50)
    private String title;
}