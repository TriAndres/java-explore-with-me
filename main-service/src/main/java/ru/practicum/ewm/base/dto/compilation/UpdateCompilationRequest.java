package ru.practicum.ewm.base.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.ewm.base.util.notBlankNull.NotBlankNull;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlankNull
    @Size(max = 128)
    private String title;
}
