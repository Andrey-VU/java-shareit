package ru.practicum.shareit.user.repo;

import ru.practicum.shareit.user.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserRepo {
    User save(User user);

    User update(User user, long id);

    Collection<User> findAll();

    Optional<User> findUser(long id);

    boolean delete(long id);

    boolean deleteAllUser(long id);
}
