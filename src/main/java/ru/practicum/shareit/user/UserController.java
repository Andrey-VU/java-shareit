package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserService;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    /*
    Здесь вам нужно сделать то же самое. Создайте класс UserController и методы в нём для основных CRUD-операций.
    Также реализуйте сохранение данных о пользователях в памяти.
     */

    public UserController(UserService userService) {
        this.userService = userService;
        }


}
