package ru.practicum.main.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import ru.practicum.main.constants.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Pattern.DATE)
    private LocalDateTime created;
    private String authorName;
    private Long eventId;
}
