package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repo.UserRepo;

import java.util.Collection;
import java.util.List;

@Service("userService")
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepo userRepo;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepo.save(UserMapper.makeUser(userDto));
        return UserMapper.makeDto(user);
    }

    @Override
    public UserDto getUser(long id) {
        if (userRepo.findUser(id).isEmpty()) {
            log.warn("User {} is not found", id);
        }
        User user = userRepo.findUser(id).orElseThrow(() -> new UserNotFoundException("Пользователь id "
                + id + " не найден"));
        return UserMapper.makeDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        User user = userRepo.update(UserMapper.makeUser(userDto), id);
        return UserMapper.makeDto(user);
    }

    @Override
    public boolean delete(long id) {
        getUser(id);
        userRepo.delete(id);
        return true;
    }

    @Override
    public void clearAll() {
        userRepo.deleteAllUser();
    }
}
