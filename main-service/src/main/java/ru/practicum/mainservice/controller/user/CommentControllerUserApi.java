package ru.practicum.mainservice.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentCreationDto;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class CommentControllerUserApi {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createUserComment(@PathVariable @Positive long userId,
                                        @PathVariable @Positive long eventId,
                                        @RequestBody @Valid CommentCreationDto commentCreationDto) {
        return commentService.addUserComment(userId, eventId, commentCreationDto);
    }

    @GetMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto getUserComment(@PathVariable @Positive long userId,
                                     @PathVariable @Positive long eventId,
                                     @PathVariable @Positive long commentId) {
        return commentService.getUserEventComment(userId, eventId, commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getUserEventComments(@PathVariable @Positive long userId,
                                                 @PathVariable @Positive long eventId) {
        return commentService.getAllUserEventComments(userId, eventId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getUserComments(@PathVariable @Positive long userId) {
        return commentService.getAllUserComments(userId);
    }

    @PatchMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto updateUserComment(@PathVariable @Positive long userId,
                                        @PathVariable @Positive long eventId,
                                        @PathVariable @Positive long commentId,
                                        @RequestBody @Valid CommentCreationDto commentCreationDto) {
        return commentService.updateUserComment(userId, eventId, commentId, commentCreationDto);
    }

    @DeleteMapping("/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserComment(@PathVariable @Positive long userId,
                                  @PathVariable @Positive long eventId,
                                  @PathVariable @Positive long commentId) {
        commentService.deleteUserComment(userId, eventId, commentId);
    }
}