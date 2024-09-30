package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.UserCommentDto;
import ru.practicum.comments.service.PublicCommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {

    private final PublicCommentService service;

    @GetMapping("/{commentId}")
    public ResponseEntity<UserCommentDto> getCommentById(@PathVariable Long commentId) {
        log.info("Получение комментария по идентификатору {}", commentId);
        UserCommentDto comment = service.getCommentById(commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/")
    public ResponseEntity<List<UserCommentDto>> getAllEventComments(@PathVariable Long eventId,
                                                                    @RequestParam(defaultValue = "0") int from,
                                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Получение всех комментарий события с идентификатором {}", eventId);
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.unsorted());
        List<UserCommentDto> comments = service.getAllEventComments(eventId, pageRequest);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
