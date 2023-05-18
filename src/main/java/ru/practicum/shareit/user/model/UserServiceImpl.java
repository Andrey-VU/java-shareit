package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import java.util.Collection;

public class UserServiceImpl implements UserService {

    /*
    Кроме DTO-классов, понадобятся Mapper-классы — они помогут преобразовывать объекты модели в DTO-объекты и обратно.
    Для базовых сущностей Item и User создайте Mapper-класс и метод преобразования объекта модели в DTO-объект.*/


    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
