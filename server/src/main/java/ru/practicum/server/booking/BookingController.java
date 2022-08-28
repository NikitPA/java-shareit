package ru.practicum.server.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.CreateBookingDto;
import ru.practicum.server.exception.ItemNotFoundException;
import ru.practicum.server.exception.UserNotFoundException;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemService;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody CreateBookingDto createBookingDto,
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
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
            @RequestHeader(header) Long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Booking> booking = bookingService.getAllBookingByUser(state, user, from, size);
        return new ResponseEntity<>(booking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("owner")
    public ResponseEntity<List<BookingDto>> getAllBookingByOwner(
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
            @RequestHeader(header) Long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Booking> booking = bookingService.getAllBookingByOwner(state, user, from, size);
        return new ResponseEntity<>(booking.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
