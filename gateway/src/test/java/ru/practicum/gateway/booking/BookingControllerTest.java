package ru.practicum.gateway.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateBookingFailedNotCorrectDateStart() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = start.plusDays(5);
        String json = "{\"start\":\"" + start + "\",\"end\":\"" + end + "\",\"itemId\":1}";

        this.mockMvc
                .perform(post("/bookings")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
