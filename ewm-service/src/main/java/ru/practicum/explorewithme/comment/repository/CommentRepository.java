package ru.practicum.explorewithme.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId, PageRequest page);

    List<Comment> findAllByAuthorId(Long userId, PageRequest page);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "WHERE LOWER(c.content) LIKE CONCAT('%', lower(?1), '%')")
    List<Comment> findAllByContent(String content, PageRequest page);
}