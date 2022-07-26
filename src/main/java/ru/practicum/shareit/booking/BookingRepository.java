package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_Owner(User user);

    List<Booking> findAllByBookerOrderByStartDateTimeDesc(User user);

    List<Booking> findAllByBookerAndStatusOrderByStartDateTimeDesc(User user, Status status);

    List<Booking> findAllByBookerAndEndDateTimeBeforeOrderByStartDateTimeDesc(User user, LocalDateTime now);

    List<Booking> findAllByBookerAndStartDateTimeAfterOrderByStartDateTimeDesc(User user, LocalDateTime now);

    List<Booking> findAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfterOrderByStartDateTimeDesc(
            User user, LocalDateTime start, LocalDateTime end
    );

    List<Booking> findAllByItem_OwnerOrderByStartDateTimeDesc(User user);

    List<Booking> findAllByItem_OwnerAndStatusOrderByStartDateTimeDesc(User user, Status status);

    List<Booking> findAllByItem_OwnerAndEndDateTimeBeforeOrderByStartDateTimeDesc(User user, LocalDateTime now);

    List<Booking> findAllByItem_OwnerAndStartDateTimeAfterOrderByStartDateTimeDesc(User user, LocalDateTime now);

    List<Booking> findAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfterOrderByStartDateTimeDesc(
            User user, LocalDateTime start, LocalDateTime end
    );
}
