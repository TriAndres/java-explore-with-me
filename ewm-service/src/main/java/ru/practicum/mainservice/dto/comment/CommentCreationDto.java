package ru.practicum.mainservice.dto.comment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreationDto {

    @NotBlank
    @Size(min = 5, max = 5000)
    private String text;
}