package ru.practicum.ewm.adminApi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.service.AdminUsersService;
import ru.practicum.ewm.base.dto.user.NewUserRequest;
import ru.practicum.ewm.base.dto.user.UserDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Slf4j
public class AdminUsersController {

    private final AdminUsersService service;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(required = false) List<Long> ids,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос get /admin/users");
        return new ResponseEntity<>(service.getAll(ids, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Получен запрос get /admin/users с новым полбзователем: {}", newUserRequest.getEmail());
        return new ResponseEntity<>(service.save(newUserRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        log.info("Получен запрос delete /admin/users/{}", userId);
        service.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
