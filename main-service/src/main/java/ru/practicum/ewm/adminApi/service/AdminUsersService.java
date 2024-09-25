package ru.practicum.ewm.adminApi.service;

import ru.practicum.ewm.base.dto.user.NewUserRequest;
import ru.practicum.ewm.base.dto.user.UserDto;

import java.util.List;

public interface AdminUsersService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);
}