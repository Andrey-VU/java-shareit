package ru.practicum.shareit.booking.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBookerIdOrderByStartDesc(Long bookerId, PageRequest of);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now, PageRequest of);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now, PageRequest of);

    List<Booking> findAllByItemIdAndStartBeforeOrderByStart(Long itemId, LocalDateTime now);

    List<Booking> findAllByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime now, LocalDateTime now1, PageRequest of);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId, PageRequest pageRequest);

    Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime now, PageRequest pageRequest);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime now, LocalDateTime now1, PageRequest pageRequest);

    Page<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime now, PageRequest pageRequest);

    Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, PageRequest pageRequest, StatusOfBooking valueOf);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, PageRequest pageRequest, StatusOfBooking valueOf);
}
