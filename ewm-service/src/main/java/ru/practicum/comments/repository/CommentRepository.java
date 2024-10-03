package ru.practicum.comments.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentStatus;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long userId, PageRequest pageRequest);

    List<Comment> findAllByEventIdAndStatus(Long eventId, CommentStatus status, PageRequest pageRequest);
}
