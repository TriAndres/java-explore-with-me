package ru.practicum.mainservice.dto.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.practicum.mainservice.model.CommentStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentStatusUpdateRequestDto {
    @NotNull
    @NotEmpty
    private List<Long> commentIds;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}