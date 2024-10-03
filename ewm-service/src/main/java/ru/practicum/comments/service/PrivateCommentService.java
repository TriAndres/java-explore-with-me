package ru.practicum.comments.service;

import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.dto.UserCommentDto;

public interface PrivateCommentService {

    UserCommentDto userAddComment(NewCommentDto newCommentDTO, Long authorId);

    UserCommentDto userUpdateComment(UpdateCommentDto updateCommentDto, Long authorId, Long commentId);

    void userDeleteComment(Long authorId, Long commentId);
}
