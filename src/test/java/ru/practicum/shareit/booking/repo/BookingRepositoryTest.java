package ru.practicum.shareit.booking.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepo;

    @Test
    void findByBookerIdOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void testFindAllByBookerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByItemIdAndStartBeforeOrderByStart() {
    }

    @Test
    void findAllByItemIdAndStartAfterOrderByStartAsc() {
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
    }
}