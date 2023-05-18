package ru.practicum.shareit.user.repo;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public class UserRepoImpl implements UserRepo {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public User findUser(long id) {
        return null;
    }

    @Override
    public boolean deleteUser(long id) {
        return false;
    }

    @Override
    public boolean deleteAllUser(long id) {
        return false;
    }
}
