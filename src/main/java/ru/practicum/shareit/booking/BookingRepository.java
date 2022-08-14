package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_Owner(User user);

    List<Booking> findAllByBooker(User user, Sort sort);

    List<Booking> findAllByBookerAndStatus(User user, Status status, Sort sort);

    List<Booking> findAllByBookerAndEndDateTimeBefore(User user, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerAndStartDateTimeAfter(User user, LocalDateTime now, Sort sort);

    List<Booking> findAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfter(
            User user, LocalDateTime start, LocalDateTime end, Sort sort
    );

    List<Booking> findAllByItem_Owner(User user, Sort sort);

    List<Booking> findAllByItem_OwnerAndStatus(User user, Status status, Sort sort);

    List<Booking> findAllByItem_OwnerAndEndDateTimeBefore(User user, LocalDateTime now, Sort sort);

    List<Booking> findAllByItem_OwnerAndStartDateTimeAfter(User user, LocalDateTime now, Sort sort);

    List<Booking> findAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfter(
            User user, LocalDateTime start, LocalDateTime end, Sort sort
    );

    List<Booking> findAllByItemAndStartDateTimeAfter(Item item, LocalDateTime now, Sort sort);

    List<Booking> findAllByItemAndEndDateTimeBefore(Item item, LocalDateTime now, Sort sort);
}
