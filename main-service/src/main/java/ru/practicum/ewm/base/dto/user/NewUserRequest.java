package ru.practicum.ewm.base.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.base.util.notBlankNull.NotBlankNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {
    @Email
    @NotBlankNull
    @Length(min = 2, max = 250)
    private String name;
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;
}