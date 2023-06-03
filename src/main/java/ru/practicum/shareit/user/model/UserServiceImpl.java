package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepo;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepo.save(UserMapper.makeUser(userDto));
        return UserMapper.makeDto(user);
    }

    @Override
    public UserDto getUser(long id) {
        if (userRepo.findById(id).isEmpty()) {
            log.warn("User {} is not found", id);
        }
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь id "
                + id + " не найден"));
        return UserMapper.makeDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepo.findAll().stream()
                .map(user -> UserMapper.makeDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        UserDto userStorage = getUser(id);
        if (userDto.getName() != null) {
            userStorage.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && userDto.getEmail().equals(userStorage.getEmail())
                || userDto.getEmail() != null && isEmailFree(userDto.getEmail())) {
            userStorage.setEmail(userDto.getEmail());
        }
        User user = UserMapper.makeUser(userStorage);

        return UserMapper.makeDto(userRepo.save(user));
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

    private boolean isEmailFree(String email) {
        if (getUsers().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .collect(Collectors.toList()).size() != 0) {
            log.warn("email {} already exists", email);
            throw new ConflictException("User with " + email + " already exists");
        }
        return true;
    }

}
