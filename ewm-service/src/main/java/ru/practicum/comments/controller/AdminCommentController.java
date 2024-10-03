package ru.practicum.comments.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.AdminCommentDto;
import ru.practicum.comments.dto.UpdateCommentStatusDto;
import ru.practicum.comments.service.AdminCommentService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {

    private final AdminCommentService commentService;

    @GetMapping("/users/{authorId}")
    public ResponseEntity<List<AdminCommentDto>> getAuthorComments(@PathVariable @Positive Long authorId,
                                                                   @RequestParam(defaultValue = "0") int from,
                                                                   @RequestParam(defaultValue = "10") int size) {
        log.info("Admin: Получение комментариев пользователя с идентификатором {}", authorId);
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.unsorted());
        List<AdminCommentDto> comments = commentService.getAuthorComments(authorId, pageRequest);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<AdminCommentDto> adminUpdateStatus(@PathVariable @Positive Long commentId,
                                                             @RequestBody UpdateCommentStatusDto updateCommentStatusDto) {
        log.info("Admin: обновлен статус на {},  комментария с идентификатором {}", updateCommentStatusDto, commentId);
        AdminCommentDto adminCommentDto = commentService.adminUpdateStatus(commentId, updateCommentStatusDto);
        return new ResponseEntity<>(adminCommentDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> adminDeleteComment(@PathVariable @Positive Long commentId) {
        log.info("Admin: удален комментарий с идентификатором {}", commentId);
        commentService.adminDeleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
