package ru.practicum.shareit.item.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepo;

    @Test
    void findAllByItemIdOrderById() {
    }
}