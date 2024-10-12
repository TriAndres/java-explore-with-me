package ru.practicum.explorewithme.comment.service;

import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> publicGetCommentByEventId(Long eventId, int from, int size);

    CommentDto privateAddComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    void privateDeleteComment(Long userId, Long commentId);

    CommentDto privatePatchComment(Long userId, Long commentId, NewCommentDto newCommentDto);

    void adminDeleteComment(Long commentId);

    List<CommentDto> adminGetCommentByUser(Long userId, int from, int size);

    List<CommentDto> adminGetCommentsSearch(String text, int from, int size);
}