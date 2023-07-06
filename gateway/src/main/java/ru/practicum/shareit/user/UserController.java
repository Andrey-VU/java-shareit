package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoGateway;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody UserDtoGateway userDto) {
        log.info("GATEWAY: Create On GateWay: {} - Started", userDto);
        ResponseEntity<Object> user = userClient.create(userDto);
        log.info("GATEWAY: create: {} - Finished", user);
        return user;
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GATEWAY: findAll - Started");
        ResponseEntity<Object> allUsers = userClient.getUsers();
        //log.info("findAll: найдено {} пользователей - Finished", allUsers.getBody());
        return allUsers;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@Positive @PathVariable("userId") long id) {
        log.info("getUser: {} - Started", id);
        ResponseEntity<Object> user = userClient.getUser(id);
        log.info("getUser: {} - Finished", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Positive @PathVariable("userId") long id,
                          @RequestBody UserDtoGateway userDto) {
        log.info("update {} for user id: {}  - Started", userDto, id);
        ResponseEntity<Object> user = userClient.update(userDto, id);
        log.info("update: {} - Finished", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@Positive @PathVariable("userId") Integer userId) {
        log.info("deleteUser: {} userId - Started", userId);
        userClient.delete(userId);
        log.info("deleteUser: {} userId - Finished", userId);
    }
}
