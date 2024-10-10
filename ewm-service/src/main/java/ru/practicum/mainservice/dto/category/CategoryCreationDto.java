package ru.practicum.mainservice.dto.category;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryCreationDto {
    @Size(min = 2, max = 50)
    @NotBlank
    private String name;
}