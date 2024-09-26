package ru.practicum.main.controllers.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.comment.CommentDto;
import ru.practicum.main.dto.comment.NewCommentDto;
import ru.practicum.main.services.CommentService;
import java.util.List;


@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getCommentsByEventId(@Positive
                                                 @RequestParam(value = "eventId") Long eventId,
                                                 @PositiveOrZero
                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                 @Positive
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return commentService.getCommentsByEventIdByAdmin(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable(value = "commentId") Long commentId) {
        return commentService.getCommentsByIdByAdmin(commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(value = "commentId") Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@RequestBody @Valid NewCommentDto newCommentDto,
                                    @PathVariable(value = "commentId") Long commentId) {
        return commentService.updateCommentByAdmin(newCommentDto, commentId);
    }
}
