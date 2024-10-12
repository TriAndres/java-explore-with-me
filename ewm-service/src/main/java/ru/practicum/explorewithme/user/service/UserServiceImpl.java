package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.util.PageCreatorUtil;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserMapper;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("Добавлен пользователь: " + user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest page = PageCreatorUtil.createPage(from, size);
        if  (ids != null) {
            List<User> users = userRepository.findAllByIdIn(ids, page);
            log.info("Возвращаю информацию о пользователях по заданным id");
            return UserMapper.listToUserDtos(users);
        } else {
            List<User> users = userRepository.findAllBy(page);
            log.info("Возвращаю информацию о всех пользователях");
            return UserMapper.listToUserDtos(users);
        }
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удалаем пользователя с id = " + id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + id + " не найден"));
        userRepository.delete(user);
    }
}
