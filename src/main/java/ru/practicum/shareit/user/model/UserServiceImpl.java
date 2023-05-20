package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repo.UserRepo;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepo userRepo;
    UserMapper mapper;

    /*
    Кроме DTO-классов, понадобятся Mapper-классы —
    они помогут преобразовывать объекты модели в DTO-объекты и обратно.
    Для базовых сущностей Item и User создайте Mapper-класс
    и метод преобразования объекта модели в DTO-объект.*/


    @Override
    public User create(UserDto userDto) {
        return userRepo.save(mapper.makeUser(userDto));
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
    public User update(UserDto userDto, long id) {
        return userRepo.update(mapper.makeUser(userDto), id);
    }

    @Override
    public boolean delete(long id) {
        getUser(id);
        userRepo.delete(id);
        return true;
    }
}
