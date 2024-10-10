package ru.practicum.mainservice.dto.comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.user.UserShortDto;


import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @NotBlank
    @Size(min = 5, max = 5000)
    private String text;

    @Column(name = "created_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserShortDto user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventShortDto event;
}
