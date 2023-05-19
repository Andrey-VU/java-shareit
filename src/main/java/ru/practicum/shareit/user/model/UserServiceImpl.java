package ru.practicum.shareit.user.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.repo.UserRepo;

import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /*
    Кроме DTO-классов, понадобятся Mapper-классы —
    они помогут преобразовывать объекты модели в DTO-объекты и обратно.
    Для базовых сущностей Item и User создайте Mapper-класс
    и метод преобразования объекта модели в DTO-объект.*/


    @Override
    public User create(User user) {
        return userRepo.save(user);
    }

    @Override
    public User getUser(long id) {
        if (userRepo.findUser(id).isEmpty()) {
            log.warn("User {} is not found", id);
        }
        User user = userRepo.findUser(id).orElseThrow(() -> new UserNotFoundException("Пользователь id "
                + id + " не найден"));
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public User update(User user, long id) {
        return userRepo.update(user, id);
    }

    @Override
    public boolean delete(long id) {
        getUser(id);
        userRepo.delete(id);
        return true;
    }
}
