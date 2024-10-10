package ru.practicum.mainservice.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
public class UserShortDto {
    @Positive
    private long id;

    @NotBlank
    private String name;
}