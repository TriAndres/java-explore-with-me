package ru.practicum.base.dto.compilation;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.base.util.notBlankNull.NotBlankNull;

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
    @Length(min = 1, max = 50)
    private String title;
}
