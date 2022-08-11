package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Validated(CreateBooking.class)
                                                    @RequestBody CreateBookingDto createBookingDto,
                                                    @RequestHeader(header) Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemService.getItemById(createBookingDto.getItemId()).orElseThrow(
                () -> new ItemNotFoundException(createBookingDto.getItemId())
        );
        Booking booking = bookingService
                .createBooking(BookingMapper.toBooking(createBookingDto, user, item, Status.WAITING), user);
        return new ResponseEntity<>(BookingMapper.toBookingDto(booking), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBooking(@RequestParam("approved") Boolean approved,
                                                    @PathVariable("bookingId") Long bookingId,
                                                    @RequestHeader(header) Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Booking booking = bookingService.updateStatus(bookingId, user, approved);
        return new ResponseEntity<>(BookingMapper.toBookingDto(booking), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable("bookingId") Long bookingId,
                                                     @RequestHeader(header) Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Booking bookingById = bookingService.getBookingById(bookingId, user);
        return new ResponseEntity<>(BookingMapper.toBookingDto(bookingById), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookingByBooker(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestHeader(header) Long userId
    ) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Booking> booking = bookingService.getAllBookingByUser(state, user);
        return new ResponseEntity<>(booking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("owner")
    public ResponseEntity<List<BookingDto>> getAllBookingByOwner(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestHeader(header) Long userId
    ) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Booking> booking = bookingService.getAllBookingByOwner(state, user);
        return new ResponseEntity<>(booking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
