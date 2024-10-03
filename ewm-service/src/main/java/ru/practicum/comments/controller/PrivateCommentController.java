package ru.practicum.comments.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.dto.UserCommentDto;
import ru.practicum.comments.service.PrivateCommentService;

@RestController
@RequestMapping("/users/{authorId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {

    private final PrivateCommentService commentService;

    @PostMapping
    public ResponseEntity<UserCommentDto> addComment(@RequestBody @Valid NewCommentDto newCommentDTO,
                                                     @PathVariable @Positive Long authorId) {
        log.info("Пользователь с идентификатором {}, добавил комментарий событию {}", authorId, newCommentDTO);
        UserCommentDto comment = commentService.userAddComment(newCommentDTO, authorId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<UserCommentDto> updateComment(@RequestBody @Valid UpdateCommentDto updateCommentDTO,
                                                        @PathVariable @Positive Long authorId,
                                                        @PathVariable @Positive Long commentId) {
        log.info("Пользователь с идентификатором {}, обновил комментарий {}", authorId, commentId);
        UserCommentDto comment = commentService.userUpdateComment(updateCommentDTO, authorId, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable @Positive Long authorId,
                                              @PathVariable @Positive Long commentId) {
        log.info("Пользователь с идентификатором {}, удалил комментарий {}", authorId, commentId);
        commentService.userDeleteComment(authorId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
