package ru.practicum.mainservice.valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.exCeption.*;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.CategoryRepository;
import ru.practicum.mainservice.repository.CommentRepository;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.UserRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class Validator {
    private final UserRepository userRepo;
    private final CategoryRepository catRepo;
    private final EventRepository eventRepo;
    private final CommentRepository commentRepo;

    public static void checkEvent1HrAhead(LocalDateTime eventDate) {
        LocalDateTime minTime = LocalDateTime.now()
                .plusHours(1);

        if (eventDate.isBefore(minTime)) {
            throw new BadRequestException("Event must start at least 1 hour from now");
        }
    }

    public static void checkEvent2HrsAhead(LocalDateTime eventDate) {
        LocalDateTime minTime = LocalDateTime.now()
                .plusHours(2);

        if (eventDate.isBefore(minTime)) {
            throw new BadRequestException("Event must start at least 2 hours from now");
        }
    }

    public User findUserOrThrow(long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id=" + userId + " not found"));
    }

    public Category findCategoryOrThrow(int categoryId) {
        return catRepo.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category id=" + categoryId + " not found"));
    }

    public Event findUserEventOrThrow(long eventId, long userId) {
        return eventRepo.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " not found"));
    }

    public Event findEventOrThrow(long eventId) {
        return eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " not found"));
    }

    public Comment findCommentOrThrow(long commentId, long userId, long eventId) {
        return commentRepo.findByIdAndUserIdAndEventId(commentId, userId, eventId)
                .orElseThrow(() -> new NotFoundException("Comment id=" + commentId + " not found"));
    }
}