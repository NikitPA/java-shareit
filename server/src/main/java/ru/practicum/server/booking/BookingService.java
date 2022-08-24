package ru.practicum.server.booking;

import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking, User user);

    Booking updateStatus(Long bookingId, User user, Boolean isStatus);

    Booking getBookingById(Long bookingId, User user);

    List<Booking> getAllBookingByUser(BookingState state, User user, int from, int size);

    List<Booking> getAllBookingByOwner(BookingState state, User user, int from, int size);

    Booking getLastBookingOfItem(Item item);

    Booking getNextBookingOfItem(Item item);
}
