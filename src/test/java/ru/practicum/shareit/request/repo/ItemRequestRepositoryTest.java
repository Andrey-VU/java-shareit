package ru.practicum.shareit.request.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepo;

    @Test
    void findAllByRequesterId() {
    }

    @Test
    void findAllNotByRequesterId() {
    }
}