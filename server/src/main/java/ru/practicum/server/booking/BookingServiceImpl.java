package ru.practicum.server.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.server.exception.BookingNotFoundException;
import ru.practicum.server.exception.ItemBelongByUserException;
import ru.practicum.server.exception.ItemNoBelongByUserException;
import ru.practicum.server.exception.ItemNotAvailableException;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private static final String nameVariable = "startDateTime";
    private final Clock clock;

    @Override
    public Booking createBooking(Booking booking, User user) {
        if (booking.getStartDateTime().isAfter(booking.getEndDateTime())) {
            throw new IllegalArgumentException("Invalid date range");
        }
        if (!booking.getItem().getAvailable()) {
            throw new ItemNotAvailableException(booking.getItem().getId());
        }
        if (Objects.equals(booking.getItem().getOwner().getId(), user.getId())) {
            throw new ItemBelongByUserException(booking.getItem().getOwner().getId(), booking.getItem().getId());
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateStatus(Long bookingId, User user, Boolean isStatus) {
        Booking booking = bookingRepository
                .findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (!Objects.equals(booking.getItem().getOwner().getId(), user.getId())) {
            throw new ItemNoBelongByUserException(booking.getItem().getId(), user.getId());
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
        if (Objects.equals(booking.getItem().getOwner().getId(), user.getId()) ||
                Objects.equals(booking.getBooker().getId(), user.getId())) {
            return booking;
        }
        throw new ItemNoBelongByUserException(booking.getItem().getId(), booking.getBooker().getId());
    }

    @Override
    public List<Booking> getAllBookingByUser(BookingState state, User user, int from, int size) {
        int page = from / size;
        PageRequest request = PageRequest.of(page, size, Sort.by(nameVariable).descending());
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBooker(user, request);
            case PAST:
                return bookingRepository
                        .findAllByBookerAndEndDateTimeBefore(
                                user, LocalDateTime.now(clock), request
                        );
            case CURRENT:
                LocalDateTime now = LocalDateTime.now(clock);
                return bookingRepository
                        .findAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfter(
                                user, now, now, request
                        );
            case FUTURE:
                return bookingRepository
                        .findAllByBookerAndStartDateTimeAfter(
                                user, LocalDateTime.now(clock), request
                        );
            case WAITING:
                return bookingRepository
                        .findAllByBookerAndStatus(
                                user, Status.WAITING, request
                        );
            case REJECTED:
                return bookingRepository
                        .findAllByBookerAndStatus(
                                user, Status.REJECTED, request
                        );
            default:
                throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<Booking> getAllBookingByOwner(BookingState state, User user, int from, int size) {
        List<Booking> allByBooker = bookingRepository.findAllByItem_Owner(user);
        if (allByBooker.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Пользователь %d не владелец хотя бы одной вещи.", user.getId())
            );
        }
        int page = from / size;
        PageRequest request = PageRequest.of(page, size, Sort.by(nameVariable).descending());
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItem_Owner(user, request);
            case PAST:
                return bookingRepository
                        .findAllByItem_OwnerAndEndDateTimeBefore(
                                user, LocalDateTime.now(clock), request
                        );
            case CURRENT:
                LocalDateTime now = LocalDateTime.now(clock);
                return bookingRepository
                        .findAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfter(
                                user, now, now, request
                        );
            case FUTURE:
                return bookingRepository
                        .findAllByItem_OwnerAndStartDateTimeAfter(
                                user, LocalDateTime.now(clock), request
                        );
            case WAITING:
                return bookingRepository
                        .findAllByItem_OwnerAndStatus(
                                user, Status.WAITING, request
                        );
            case REJECTED:
                return bookingRepository
                        .findAllByItem_OwnerAndStatus(
                                user, Status.REJECTED, request
                        );
            default:
                throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public Booking getLastBookingOfItem(Item item) {
        LocalDateTime now = LocalDateTime.now(clock);
        return bookingRepository
                .findAllByItemAndEndDateTimeBefore(item, now, Sort.by(nameVariable).descending())
                .stream()
                .max(Comparator.comparing(Booking::getEndDateTime))
                .orElse(null);
    }

    @Override
    public Booking getNextBookingOfItem(Item item) {
        LocalDateTime now = LocalDateTime.now(clock);
        return bookingRepository
                .findAllByItemAndStartDateTimeAfter(item, now, Sort.by(nameVariable).descending())
                .stream()
                .findFirst()
                .orElse(null);
    }
}
