package ru.practicum.explorewithme.comment.dto;

import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.event.dto.EventMapper;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.dto.UserMapper;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommentDto toCommentDto(Comment comment) {
        String edited;
        if (comment.getEdited() != null) {
            edited = comment.getEdited().format(FORMATTER);
        } else {
            edited = null;
        }
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                UserMapper.toUserDto(comment.getAuthor()),
                EventMapper.toEventCommentDto(comment.getEvent()),
                comment.getCreated().format(FORMATTER),
                edited
        );
    }

    public static Comment toComment(NewCommentDto newCommentDto, User author, Event event, LocalDateTime created, LocalDateTime edited) {
        return new Comment(
                null,
                newCommentDto.getContent(),
                author,
                event,
                created,
                edited
        );
    }

    public static List<CommentDto> listToCommentDtos(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(toCommentDto(comment));
        }
        return dtos;
    }
}
