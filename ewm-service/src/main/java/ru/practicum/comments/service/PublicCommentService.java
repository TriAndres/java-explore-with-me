package ru.practicum.comments.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.comments.dto.UserCommentDto;

import java.util.List;

public interface PublicCommentService {

    UserCommentDto getCommentById(Long commentId);

    List<UserCommentDto> getAllEventComments(Long eventId, PageRequest pageRequest);
}
