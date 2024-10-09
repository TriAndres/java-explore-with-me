package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.UserCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ViolationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final CommentMapper commentMapper;

    @Override
    public UserCommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не найден"));

        if (comment.getStatus() != CommentStatus.PUBLISHED) {
            throw new ViolationException("Комментарий должен быть опубликован");
        }

        Long eventId = comment.getEvent().getId();

        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));

        return commentMapper.commentToUserCommentDto(comment);
    }

    @Override
    public List<UserCommentDto> getAllEventComments(Long eventId, PageRequest pageRequest) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));

        List<Comment> comments = commentRepository
                .findAllByEventIdAndStatus(eventId, CommentStatus.PUBLISHED, pageRequest);

        return comments.stream().map(commentMapper::commentToUserCommentDto).collect(Collectors.toList());
    }
}
