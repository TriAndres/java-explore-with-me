package ru.practicum.comments.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.comments.dto.AdminCommentDto;
import ru.practicum.comments.dto.UpdateCommentStatusDto;

import java.util.List;

public interface AdminCommentService {
    List<AdminCommentDto> getAuthorComments(Long authorId, PageRequest pageRequest);

    AdminCommentDto adminUpdateStatus(Long commentId, UpdateCommentStatusDto updateCommentStatusDto);

    void adminDeleteComment(Long commentId);
}
