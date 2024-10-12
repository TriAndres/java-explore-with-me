package ru.practicum.explorewithme.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserDto {

    private Long id;

    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @NotBlank
    @Email
    @Length(min = 6, max = 254)
    private String email;
}