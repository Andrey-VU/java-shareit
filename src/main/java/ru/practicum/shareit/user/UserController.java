package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        log.info("create: {} - Started", userDto);
        User user = userService.create(userDto);
        log.info("create: {} - Finished", user);
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("findAll - Started");
        Collection<User> allUsers = userService.getUsers();
        log.info("findAll: найдено {} пользователей - Finished", allUsers.size());
        return allUsers;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") long id) {
        log.info("getUser: {} - Started", id);
        User user = userService.getUser(id);
        log.info("getUser: {} - Finished", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable("userId") long id,
                       @RequestBody UserDto userDto) {
        log.info("update {} for user id: {}  - Started", userDto, id);
        User user = userService.update(userDto, id);
        log.info("update: {} - Finished", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        log.info("deleteUser: {} userId - Started", userId);
        boolean isDel = userService.delete(userId);
        log.info("deleteUser: {} userId - Finished {} ", userId, isDel);
    }
}
