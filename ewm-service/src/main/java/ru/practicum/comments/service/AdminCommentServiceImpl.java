package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.AdminCommentDto;
import ru.practicum.comments.dto.UpdateCommentStatusDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.comments.model.CommentStatus.CANCELED;
import static ru.practicum.comments.model.CommentStatus.PUBLISHED;

@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    @Override
    public List<AdminCommentDto> getAuthorComments(Long authorId, PageRequest pageRequest) {
        userRepository.findById(authorId).orElseThrow(()
                -> new NotFoundException("Пользователь с id \"" + authorId + "\" не найден"));
        List<Comment> comments = commentRepository.findByAuthorId(authorId, pageRequest);
        return comments.stream().map(commentMapper::commentToAdminCommentDto).collect(Collectors.toList());
    }

    @Override
    public AdminCommentDto adminUpdateStatus(Long commentId, UpdateCommentStatusDto updateCommentStatusDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден."));
        String stateAction = updateCommentStatusDto.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case "REJECT_COMMENT" -> comment.setStatus(CANCELED);
                case "PUBLISH_COMMENT" -> comment.setStatus(PUBLISHED);
                default -> throw new ValidationException("Такого действия не существует - " + stateAction);
            }
        }
        return commentMapper.commentToAdminCommentDto(commentRepository.save(comment));
    }

    @Override
    public void adminDeleteComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не найден."));
        commentRepository.deleteById(commentId);
    }
}