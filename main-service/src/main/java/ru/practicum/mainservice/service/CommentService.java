package ru.practicum.mainservice.service;

import ru.practicum.mainservice.dto.comment.CommentCreationDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentStatusUpdateRequestDto;
import ru.practicum.mainservice.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentDto addUserComment(long userId, long eventId, CommentCreationDto commentCreationDto);

    CommentDto updateUserComment(long userId, long eventId, long commentId, CommentCreationDto commentCreationDto);

    void deleteUserComment(long userId, long eventId, long commentId);

    CommentDto getUserEventComment(long userId, long eventId, long commentId);

    List<CommentDto> getAllUserComments(long userId);

    List<CommentDto> getAllUserEventComments(long userId, long eventId);

    List<CommentDto> getAdminComments(String text, List<Long> users, List<CommentStatus> statuses, List<Long> events,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<CommentDto> moderateAdminComments(CommentStatusUpdateRequestDto updateRequest);
}