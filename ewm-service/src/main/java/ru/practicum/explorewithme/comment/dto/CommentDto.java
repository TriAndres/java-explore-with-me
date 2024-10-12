package ru.practicum.explorewithme.comment.dto;

import lombok.*;
import ru.practicum.explorewithme.event.dto.EventCommentDto;
import ru.practicum.explorewithme.user.dto.UserDto;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private UserDto author;
    private EventCommentDto event;
    private String created;
    private String edited;
}