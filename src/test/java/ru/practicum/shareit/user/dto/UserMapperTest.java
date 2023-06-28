package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    UserDto newUserDto;
    User user;

    @BeforeEach
    void setUp() {
        newUserDto = UserDto.builder()
                .email("nu@nu.nu")
                .name("New User")
                .build();

        user = User.builder()
                .email("nu@nu.nu")
                .name("New User")
                .id(1L)
                .build();
    }

    @Test
    void makeUser_whenUserDtoCorrect_thenReturnUser() {

        User userFromMapper = UserMapper.makeUser(newUserDto).orElseThrow();
        userFromMapper.setId(1L);
        assertEquals(user, userFromMapper);

    }

    @Test
    void makeDto() {
    }

    @Test
    void makeUserWithId() {
    }
}