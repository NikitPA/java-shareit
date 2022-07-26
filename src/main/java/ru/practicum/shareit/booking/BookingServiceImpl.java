package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemBelongByUserException;
import ru.practicum.shareit.exception.ItemNoBelongByUserException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking, User user) {
        if (booking.getStartDateTime().isAfter(booking.getEndDateTime())) {
            throw new IllegalArgumentException("Invalid date range");
        }
        if (!booking.getItem().getAvailable()) {
            throw new ItemNotAvailableException(booking.getItem().getId());
        }
        if (booking.getItem().getOwner().getId() == user.getId()) {
            throw new ItemBelongByUserException(booking.getItem().getOwner().getId(), booking.getItem().getId());
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateStatus(Long bookingId, User user, Boolean isStatus) {
        Booking booking = bookingRepository
                .findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (booking.getItem().getOwner().getId() != user.getId()) {
            throw new ItemNoBelongByUserException(booking.getItem().getId(), booking.getBooker().getId());
        }
        if (!booking.getItem().getAvailable()) {
            throw new ItemNotAvailableException(booking.getItem().getId());
        }
        if (booking.getStatus() != Status.WAITING) {
            throw new IllegalArgumentException("Не ожидается подтверждение");
        }
        booking.setStatus(isStatus ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId, User user) {
        Booking booking = bookingRepository
                .findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (booking.getItem().getOwner().getId() == user.getId() || booking.getBooker().getId() == user.getId()) {
            return booking;
        }
        throw new ItemNoBelongByUserException(booking.getItem().getId(), booking.getBooker().getId());
    }

    @Override
    public List<Booking> getAllBookingByUser(String state, User user) {
        switch (state) {
            case ("ALL"):
                return bookingRepository.findAllByBookerOrderByStartDateTimeDesc(user);
            case ("PAST"):
                return bookingRepository
                        .findAllByBookerAndEndDateTimeBeforeOrderByStartDateTimeDesc(user, LocalDateTime.now());
            case ("CURRENT"):
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository
                        .findAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfterOrderByStartDateTimeDesc(
                                user, now, now
                        );
            case ("FUTURE"):
                return bookingRepository
                        .findAllByBookerAndStartDateTimeAfterOrderByStartDateTimeDesc(user, LocalDateTime.now());
            case ("WAITING"):
                return bookingRepository
                        .findAllByBookerAndStatusOrderByStartDateTimeDesc(user, Status.WAITING);
            case ("REJECTED"):
                return bookingRepository
                        .findAllByBookerAndStatusOrderByStartDateTimeDesc(user, Status.REJECTED);
            default:
                throw new IllegalArgumentException("Передано не соответствующее состояние бронирования.");
        }
    }

    @Override
    public List<Booking> getAllBookingByOwner(String state, User user) {
        List<Booking> allByBooker = bookingRepository.findAllByItem_Owner(user);
        if (allByBooker.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Пользователь %d не владелец хотя бы одной вещи.", user.getId())
            );
        }
        switch (state) {
            case ("ALL"):
                return bookingRepository.findAllByItem_OwnerOrderByStartDateTimeDesc(user);
            case ("PAST"):
                return bookingRepository
                        .findAllByItem_OwnerAndEndDateTimeBeforeOrderByStartDateTimeDesc(user, LocalDateTime.now());
            case ("CURRENT"):
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository
                        .findAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfterOrderByStartDateTimeDesc(
                                user, now, now
                        );
            case ("FUTURE"):
                return bookingRepository
                        .findAllByItem_OwnerAndStartDateTimeAfterOrderByStartDateTimeDesc(user, LocalDateTime.now());
            case ("WAITING"):
                return bookingRepository
                        .findAllByItem_OwnerAndStatusOrderByStartDateTimeDesc(user, Status.WAITING);
            case ("REJECTED"):
                return bookingRepository
                        .findAllByItem_OwnerAndStatusOrderByStartDateTimeDesc(user, Status.REJECTED);
            default:
                throw new IllegalArgumentException("Передано не соответствующее состояние бронирования.");
        }
    }

    @Override
    public Booking getLastBookingOfItem(User user, LocalDateTime now) {
        return bookingRepository
                .findAllByItem_OwnerAndEndDateTimeBeforeOrderByStartDateTimeDesc(user, now)
                .stream()
                .max(Comparator.comparing(Booking::getEndDateTime))
                .orElse(null);
    }

    @Override
    public Booking getNextBookingOfItem(User user, LocalDateTime now) {
        return bookingRepository
                .findAllByItem_OwnerAndStartDateTimeAfterOrderByStartDateTimeDesc(user, now)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
