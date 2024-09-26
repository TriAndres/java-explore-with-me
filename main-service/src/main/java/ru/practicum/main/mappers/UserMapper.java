package ru.practicum.main.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.main.dto.user.UserDto;
import ru.practicum.main.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User toUserModel(UserDto userModelDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> usersList);
}
