package ru.practicum.server.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.server.booking.*;
import ru.practicum.server.exception.ItemBelongByUserException;
import ru.practicum.server.exception.ItemNoBelongByUserException;
import ru.practicum.server.exception.ItemNotAvailableException;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.catchThrowableOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository repository;

    @MockBean
    private Clock clock;

    @Test
    void testCreateBooking() {
        //Given
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.plusDays(1);

        User user = mock(User.class);
        doReturn(2L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(start).when(booking).getStartDateTime();
        doReturn(end).when(booking).getEndDateTime();

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(true).when(item).getAvailable();
        doReturn(1L).when(owner).getId();

        //When
        bookingService.createBooking(booking, user);

        //Then
        verify(repository, times(1)).save(booking);
    }

    @Test
    void testCreateBookingFailedTimeEndBeforeStart() {
        //Given
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.minusDays(1);
        User user = mock(User.class);

        Booking booking = mock(Booking.class);
        doReturn(start).when(booking).getStartDateTime();
        doReturn(end).when(booking).getEndDateTime();

        //When

        //Then
        catchIllegalArgumentException(() -> bookingService.createBooking(booking, user));
        verify(repository, times(0)).save(booking);
    }

    @Test
    void testCreateBookingFailedNoAvailableItem() {
        //Given
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.plusDays(1);

        User user = mock(User.class);

        Booking booking = mock(Booking.class);
        doReturn(start).when(booking).getStartDateTime();
        doReturn(end).when(booking).getEndDateTime();

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(false).when(item).getAvailable();

        //When

        //Then
        catchThrowableOfType(() -> bookingService.createBooking(booking, user), ItemNotAvailableException.class);
        verify(repository, times(0)).save(booking);
    }

    @Test
    void testCreateBookingFailedItemBelongUser() {
        //Given
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.plusDays(1);

        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(start).when(booking).getStartDateTime();
        doReturn(end).when(booking).getEndDateTime();

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(true).when(item).getAvailable();
        doReturn(1L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(() -> bookingService.createBooking(booking, user), ItemBelongByUserException.class);
        verify(repository, times(0)).save(booking);
    }

    @Test
    void testUpdateStatusTrue() {
        //Given
        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(Status.WAITING).when(booking).getStatus();
        doReturn(Optional.of(booking)).when(repository).findById(1L);

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(true).when(item).getAvailable();
        doReturn(1L).when(owner).getId();

        //When
        bookingService.updateStatus(1L, user, true);

        //Then
        verify(repository, times(1)).save(booking);
        verify(booking, times(1)).setStatus(Status.APPROVED);
    }

    @Test
    void testUpdateStatusFalse() {
        //Given
        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(Status.WAITING).when(booking).getStatus();
        doReturn(Optional.of(booking)).when(repository).findById(1L);

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(true).when(item).getAvailable();
        doReturn(1L).when(owner).getId();

        //When
        bookingService.updateStatus(1L, user, false);

        //Then
        verify(repository, times(1)).save(booking);
        verify(booking, times(1)).setStatus(Status.REJECTED);
    }

    @Test
    void testUpdateStatusFailedItemNotBelongUser() {
        //Given
        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(Optional.of(booking)).when(repository).findById(1L);

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(2L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(
                () -> bookingService.updateStatus(1L, user, false), ItemNoBelongByUserException.class
        );
        verify(repository, times(0)).save(booking);
    }

    @Test
    void testUpdateStatusFailedNoAvailable() {
        //Given
        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(Optional.of(booking)).when(repository).findById(1L);

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(false).when(item).getAvailable();
        doReturn(1L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(
                () -> bookingService.updateStatus(1L, user, false), ItemNotAvailableException.class)
        ;
        verify(repository, times(0)).save(booking);
    }

    @Test
    void testUpdateStatusFailedStatusNotWaiting() {
        //Given
        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(Status.APPROVED).when(booking).getStatus();
        doReturn(Optional.of(booking)).when(repository).findById(1L);

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(true).when(item).getAvailable();
        doReturn(1L).when(owner).getId();

        //When

        //Then
        catchIllegalArgumentException(() -> bookingService.updateStatus(1L, user, false));
        verify(repository, times(0)).save(booking);
    }

    @Test
    void testGetBookingById() {
        //Given
        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(Optional.of(booking)).when(repository).findById(1L);

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(1L).when(owner).getId();

        //When
        bookingService.getBookingById(1L, user);

        //Then
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetBookingByIdFailedBookingNoBelongUserAndItemNoBelongUser() {
        //Given
        User user = mock(User.class);
        doReturn(1L).when(user).getId();

        User owner = mock(User.class);
        User booker = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(booker).when(booking).getBooker();
        doReturn(2L).when(booker).getId();
        doReturn(Optional.of(booking)).when(repository).findById(1L);

        Item item = mock(Item.class);
        doReturn(item).when(booking).getItem();
        doReturn(owner).when(item).getOwner();
        doReturn(2L).when(owner).getId();

        //When

        //Then
        catchThrowableOfType(
                () -> bookingService.getBookingById(1L, user), ItemNoBelongByUserException.class
        );
    }

    @Test
    void testGetAllBookingByUser_StatusPast() {
        //Given
        User user = mock(User.class);
        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getAllBookingByUser(BookingState.PAST, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByBookerAndEndDateTimeBefore(
                        user, now, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByUser_StatusCurrent() {
        //Given
        User user = mock(User.class);
        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getAllBookingByUser(BookingState.CURRENT, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByBookerAndStartDateTimeBeforeAndEndDateTimeAfter(
                        user, now, now, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByUser_StatusFuture() {
        //Given
        User user = mock(User.class);
        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getAllBookingByUser(BookingState.FUTURE, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByBookerAndStartDateTimeAfter(
                        user, now, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByUser_StatusWaiting() {
        //Given
        User user = mock(User.class);

        //When
        bookingService.getAllBookingByUser(BookingState.WAITING, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByBookerAndStatus(
                        user, Status.WAITING, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByUser_StatusRejected() {
        //Given
        User user = mock(User.class);

        //When
        bookingService.getAllBookingByUser(BookingState.REJECTED, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByBookerAndStatus(
                        user, Status.REJECTED, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByUser_All() {
        //Given
        User user = mock(User.class);

        //When
        bookingService.getAllBookingByUser(BookingState.ALL, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByBooker(user, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByOwner_UserNoBelongSingleItem() {
        //Given
        User user = mock(User.class);

        //When

        //Then
        catchIllegalArgumentException(() -> bookingService.getAllBookingByOwner(BookingState.ALL, user, 0, 10));
    }

    @Test
    void testGetAllBookingByOwner_StatusAll() {
        //Given
        User user = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(List.of(booking)).when(repository).findAllByItem_Owner(user);

        //When
        bookingService.getAllBookingByOwner(BookingState.ALL, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByItem_Owner(user, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByOwner_StatusPast() {
        //Given
        User user = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(List.of(booking)).when(repository).findAllByItem_Owner(user);

        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getAllBookingByOwner(BookingState.PAST, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByItem_OwnerAndEndDateTimeBefore(user, now, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByOwner_StatusCurrent() {
        //Given
        User user = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(List.of(booking)).when(repository).findAllByItem_Owner(user);

        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getAllBookingByOwner(BookingState.CURRENT, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByItem_OwnerAndStartDateTimeBeforeAndEndDateTimeAfter(
                        user, now, now, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByOwner_StatusFuture() {
        //Given
        User user = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(List.of(booking)).when(repository).findAllByItem_Owner(user);

        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getAllBookingByOwner(BookingState.FUTURE, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByItem_OwnerAndStartDateTimeAfter(user, now, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByOwner_StatusWaiting() {
        //Given
        User user = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(List.of(booking)).when(repository).findAllByItem_Owner(user);

        //When
        bookingService.getAllBookingByOwner(BookingState.WAITING, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByItem_OwnerAndStatus(user, Status.WAITING, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetAllBookingByOwner_StatusRejected() {
        //Given
        User user = mock(User.class);
        Booking booking = mock(Booking.class);
        doReturn(List.of(booking)).when(repository).findAllByItem_Owner(user);

        //When
        bookingService.getAllBookingByOwner(BookingState.REJECTED, user, 0, 10);

        //Then
        verify(repository, times(1))
                .findAllByItem_OwnerAndStatus(
                        user, Status.REJECTED, PageRequest.of(0, 10, Sort.by("startDateTime").descending())
                );
    }

    @Test
    void testGetLastBookingOfItem() {
        //Given
        Item item = mock(Item.class);
        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getLastBookingOfItem(item);

        //Then
        verify(repository, times(1))
                .findAllByItemAndEndDateTimeBefore(item, now, Sort.by("startDateTime").descending());
    }

    @Test
    void testGetNextBookingOfItem() {
        //Given
        Item item = mock(Item.class);
        doReturn(Instant.parse("2014-12-22T10:15:30.00Z")).when(clock).instant();
        doReturn(ZoneId.of("UTC")).when(clock).getZone();
        final LocalDateTime now = LocalDateTime.now(clock);

        //When
        bookingService.getNextBookingOfItem(item);

        //Then
        verify(repository, times(1))
                .findAllByItemAndStartDateTimeAfter(item, now, Sort.by("startDateTime").descending());
    }
}
