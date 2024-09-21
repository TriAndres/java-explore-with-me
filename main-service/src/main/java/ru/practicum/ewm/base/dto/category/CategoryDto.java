package ru.practicum.ewm.base.dto.category;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    @Length(min = 1, max = 50)
    private String name;
}