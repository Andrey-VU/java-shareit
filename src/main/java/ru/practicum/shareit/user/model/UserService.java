package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User create(User user);
    User getUser(long id);
    Collection<User> getUsers();
    User update(User user, long id);
    boolean delete(long id);
}
