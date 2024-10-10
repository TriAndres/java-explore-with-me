package ru.practicum.mainservice.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentStatusUpdateRequestDto;
import ru.practicum.mainservice.model.CommentStatus;
import ru.practicum.mainservice.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentControllerAdminApi {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAdminComments(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "statuses", required = false) List<CommentStatus> statuses,
                                             @RequestParam(name = "events", required = false) List<Long> events,
                                             @RequestParam(name = "rangeStart", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return commentService.getAdminComments(text, users, statuses, events, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping
    public List<CommentDto> moderateAdminComments(@RequestBody CommentStatusUpdateRequestDto updateRequest) {
        return commentService.moderateAdminComments(updateRequest);
    }
}