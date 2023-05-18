package ru.practicum.shareit.user.repo;

import ru.practicum.shareit.user.model.User;
import java.util.Collection;

public interface UserRepo {
    User save(User user);
    Collection<User> findAll();
    User findUser(long id);
    boolean deleteUser(long id);
    boolean deleteAllUser(long id);
}
