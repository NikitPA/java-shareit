package ru.practicum.server.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_Owner(User user);

    List<Booking> findAllByBooker(User user, Pageable pageable);

    List<Booking> findAllByBookerAndStatus(User user, Status status, Pageable pageable);

    List<Booking> findAllByBookerAndEndDateTimeBefore(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerAndStartDateTimeAfter(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfter(
            User user, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    List<Booking> findAllByItem_Owner(User user, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStatus(User user, Status status, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndEndDateTimeBefore(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStartDateTimeAfter(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfter(
            User user, LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    List<Booking> findAllByItemAndStartDateTimeAfter(Item item, LocalDateTime now, Sort sort);

    List<Booking> findAllByItemAndEndDateTimeBefore(Item item, LocalDateTime now, Sort sort);
}
