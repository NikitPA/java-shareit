package ru.practicum.server.booking;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.item.Item;
import ru.practicum.server.item.ItemService;
import ru.practicum.server.user.User;
import ru.practicum.server.user.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateBooking() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(5);
        LocalDateTime end = start.plusDays(5);
        String json = "{\"start\":\"" + start + "\",\"end\":\"" + end + "\",\"itemId\":1}";

        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        Item item = Mockito.mock(Item.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);

        try (MockedStatic<BookingMapper> utilities = Mockito.mockStatic(BookingMapper.class)) {
            utilities.when(() -> BookingMapper.toBookingDto(Mockito.mock(Booking.class)))
                    .thenReturn(Mockito.mock(BookingDto.class));

            this.mockMvc
                    .perform(post("/bookings")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isCreated())
                    .andDo(print());
        }
    }

    @Test
    void testCreateBookingFailedUserNotFound() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(5);
        LocalDateTime end = start.plusDays(5);
        String json = "{\"start\":\"" + start + "\",\"end\":\"" + end + "\",\"itemId\":1}";

        Item item = Mockito.mock(Item.class);
        doReturn(Optional.of(item)).when(itemService).getItemById(1L);

        this.mockMvc
                .perform(post("/bookings")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testCreateBookingFailedItemNotFound() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(5);
        LocalDateTime end = start.plusDays(5);
        String json = "{\"start\":\"" + start + "\",\"end\":\"" + end + "\",\"itemId\":1}";

        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        this.mockMvc
                .perform(post("/bookings")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testUpdateBooking() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        try (MockedStatic<BookingMapper> utilities = Mockito.mockStatic(BookingMapper.class)) {
            utilities.when(() -> BookingMapper.toBookingDto(Mockito.mock(Booking.class)))
                    .thenReturn(Mockito.mock(BookingDto.class));

            this.mockMvc
                    .perform(patch("/bookings/1")
                            .param("approved", "true")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testUpdateBookingFailedApprovedNotCorrect() throws Exception {
        this.mockMvc
                .perform(patch("/bookings/1")
                        .param("approved", "")
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testGetBookingById() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        try (MockedStatic<BookingMapper> utilities = Mockito.mockStatic(BookingMapper.class)) {
            utilities.when(() -> BookingMapper.toBookingDto(Mockito.mock(Booking.class)))
                    .thenReturn(Mockito.mock(BookingDto.class));

            this.mockMvc
                    .perform(get("/bookings/1")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testGetBookingByIdFailedUserNotFound() throws Exception {
        this.mockMvc
                .perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testGetAllBookingByBooker() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        try (MockedStatic<BookingMapper> utilities = Mockito.mockStatic(BookingMapper.class)) {
            utilities.when(() -> BookingMapper.toBookingDto(Mockito.mock(Booking.class)))
                    .thenReturn(Mockito.mock(BookingDto.class));

            this.mockMvc
                    .perform(get("/bookings")
                            .param("state", "WAITING")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void testGetAllBookingByOwner() throws Exception {
        User user = Mockito.mock(User.class);
        doReturn(Optional.of(user)).when(userService).getUserById(3L);

        try (MockedStatic<BookingMapper> utilities = Mockito.mockStatic(BookingMapper.class)) {
            utilities.when(() -> BookingMapper.toBookingDto(Mockito.mock(Booking.class)))
                    .thenReturn(Mockito.mock(BookingDto.class));

            this.mockMvc
                    .perform(get("/bookings/owner")
                            .param("state", "WAITING")
                            .header("X-Sharer-User-Id", 3))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }
}
