package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.comment.CommentCreationDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.model.Comment;

public class CommentMapper {
    public static Comment commentCreationDtoToComment(CommentCreationDto commentCreationDto) {
        return Comment.builder()
                .text(commentCreationDto.getText())
                .build();
    }

    public static CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .user(UserMapper.toUserShortDto(comment.getUser()))
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .build();
    }
}