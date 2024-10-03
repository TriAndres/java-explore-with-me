package ru.practicum.requests.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addRequest(@PathVariable @Positive Long userId,
                                                              @RequestParam @Positive Long eventId) {
        log.info("Добавление запроса на участие в событии {}, от пользователя с идентификатором {}", eventId, userId);
        ParticipationRequestDto participationRequestDto = requestService.addRequest(userId, eventId);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getAllRequests(@PathVariable @Positive Long userId) {
        log.info("Получение информации о заявках пользователя {}, на участие в чужих событиях", userId);
        List<ParticipationRequestDto> requests = requestService.getAllRequests(userId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long requestId) {
        log.info("Отмена запроса на участие в событии {}, от пользователя с идентификатором {}", requestId, userId);
        ParticipationRequestDto participationRequestDto = requestService.cancelRequest(userId, requestId);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }
}
