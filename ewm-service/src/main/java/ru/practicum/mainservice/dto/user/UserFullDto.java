package ru.practicum.mainservice.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFullDto {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;
}