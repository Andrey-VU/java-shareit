package ru.practicum.shareit.user.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Deprecated
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void findAll() {
    }

}