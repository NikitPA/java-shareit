package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking, User user);

    Booking updateStatus(Long bookingId, User user, Boolean isStatus);

    Booking getBookingById(Long bookingId, User user);

    List<Booking> getAllBookingByUser(String state, User user);

    List<Booking> getAllBookingByOwner(String state, User user);

    Booking getLastBookingOfItem(Item item);

    Booking getNextBookingOfItem(Item item);
}
