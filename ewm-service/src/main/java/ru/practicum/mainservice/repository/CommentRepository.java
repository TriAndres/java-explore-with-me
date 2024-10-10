package ru.practicum.mainservice.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    static Specification<Comment> hasText(String text) {
        return (event, query, builder) -> {
            if (text == null) {
                return builder.isTrue(builder.literal(true));
            } else {
                return builder.like(builder.lower(event.get("text")), "%" + text.toLowerCase() + "%");
            }
        };
    }

    static Specification<Comment> hasEvent(List<Long> events) {
        return (event, query, builder) -> {
            if (events == null || events.isEmpty()) {
                return builder.isTrue(builder.literal(true));
            } else {
                return events.stream()
                        .reduce(builder.in(event.get("event")), CriteriaBuilder.In::value, (a, b) -> a);
            }
        };
    }

    static Specification<Comment> hasUser(List<Long> users) {
        return (event, query, builder) -> {
            if (users == null || users.isEmpty()) {
                return builder.isTrue(builder.literal(true));
            } else {
                return users.stream()
                        .reduce(builder.in(event.get("commenter")), CriteriaBuilder.In::value, (a, b) -> a);
            }
        };
    }

    static Specification<Comment> hasStatus(List<CommentStatus> statuses) {
        return (event, query, builder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return builder.isTrue(builder.literal(true));
            } else {
                return statuses.stream()
                        .reduce(builder.in(event.get("status")), CriteriaBuilder.In::value, (a, b) -> a);
            }
        };
    }

    static Specification<Comment> isAfter(LocalDateTime dateTime) {
        return (event, query, builder) -> {
            if (dateTime == null) {
                return builder.isTrue(builder.literal(true));
            } else {
                return builder.greaterThan(event.get("createdOn"), dateTime);
            }
        };
    }

    static Specification<Comment> isBefore(LocalDateTime dateTime) {
        return (event, query, builder) -> {
            if (dateTime == null) {
                return builder.isTrue(builder.literal(true));
            } else {
                return builder.lessThan(event.get("createdOn"), dateTime);
            }
        };
    }

    Optional<Comment> findByIdAndUserIdAndEventId(long commentId, long userId, long eventId);

    List<Comment> findAllByUserIdAndStatus(long userId, CommentStatus published);

    List<Comment> findAllByUserIdAndEventIdAndStatus(long userId, long eventId, CommentStatus published);

    void deleteByIdAndUserIdAndEventIdAndStatus(long commentId, long userId, long eventId, CommentStatus published);

    List<Comment> findAllByIdInAndStatus(List<Long> commentIds, CommentStatus status);

    boolean existsByIdAndUserIdAndEventIdAndStatus(long commentId, long userId, long eventId, CommentStatus published);
}