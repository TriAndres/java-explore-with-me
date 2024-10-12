package ru.practicum.explorewithme.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.CommentMapper;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.exception.CommentNotFoundException;
import ru.practicum.explorewithme.comment.exception.NotAuthorException;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.comment.repository.CommentRepository;
import ru.practicum.explorewithme.common.util.PageCreatorUtil;
import ru.practicum.explorewithme.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.request.exception.EventIsNotPublishedException;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CommentServiceImpl implements CommentService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> publicGetCommentByEventId(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        PageRequest page = PageCreatorUtil.createPage(from, size, "created");
        List<Comment> comments = commentRepository.findAllByEventId(eventId, page);
        log.info("Найдены комментарии: " + comments);
        return CommentMapper.listToCommentDtos(comments);
    }

    @Override
    public CommentDto privateAddComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("События с id = " + eventId + " не существует"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventIsNotPublishedException("Нельзя оставить комментарий неопубликованному событию");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(newCommentDto, author, event, LocalDateTime.now(), null));
        log.info("Комментарий: " + comment + " сохранен");
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void privateDeleteComment(Long userId, Long commentId) {
        User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Пользователя с id = " + commentId + " не существует"));
        if (!Objects.equals(comment.getAuthor().getId(), author.getId())) {
            throw new NotAuthorException("Пользователь с id = " + userId + " не является автором комментария");
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto privatePatchComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!Objects.equals(comment.getAuthor().getId(), author.getId())) {
            throw new NotAuthorException("Пользователь с id = " + userId + " не является автором комментария");
        }
        comment.setContent(newCommentDto.getContent());
        comment.setEdited(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void adminDeleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Пользователя с id = " + commentId + " не существует"));
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDto> adminGetCommentByUser(Long userId, int from, int size) {
        User author = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        PageRequest page = PageCreatorUtil.createPage(from, size, "created");
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, page);
        log.info("Найдены комментарии: " + comments);
        return CommentMapper.listToCommentDtos(comments);
    }

    @Override
    public List<CommentDto> adminGetCommentsSearch(String text, int from, int size) {
        PageRequest page = PageCreatorUtil.createPage(from, size, "created");
        List<Comment> comments = commentRepository.findAllByContent(text, page);
        return CommentMapper.listToCommentDtos(comments);
    }
}
