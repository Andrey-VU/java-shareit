package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service()
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepo;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepo.save(UserMapper.makeUser(userDto)
                .orElseThrow(() -> new NullPointerException("User объект не создан")));
        return UserMapper.makeDto(user)
                .orElseThrow(() -> new NullPointerException("dto объект не найден"));
    }

    @Override
    public UserDto getUser(long id) {
        if (userRepo.findById(id).isEmpty()) {
            log.warn("User {} is not found", id);
        }
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь id "
                + id + " не найден"));
        return UserMapper.makeDto(user)
                .orElseThrow(() -> new NullPointerException("dto объект не найден"));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepo.findAll().stream()
                .map(user -> UserMapper.makeDto(user)
                        .orElseThrow(() -> new NullPointerException("dto объект не найден")))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        UserDto userStorage = getUser(id);
        if (userDto.getName() != null) {
            userStorage.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userStorage.setEmail(userDto.getEmail());
        }
        User user = UserMapper.makeUserWithId(userStorage)
                .orElseThrow(() -> new NullPointerException("объект не найден"));

        return UserMapper.makeDto(userRepo.save(user))
                .orElseThrow(() -> new NullPointerException("dto объект не найден"));
    }

    @Override
    public boolean delete(long id) {
        getUser(id);
        userRepo.deleteById(id);
        return true;
    }

    @Override
    public void clearAll() {
        userRepo.deleteAll();
    }
}
