package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User create(UserDto userDto);
    User getUser(long id);
    Collection<User> getUsers();
    User update(UserDto userDto, long id);
    boolean delete(long id);
}
