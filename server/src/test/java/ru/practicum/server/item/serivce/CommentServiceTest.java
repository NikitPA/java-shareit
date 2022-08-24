package ru.practicum.server.item.serivce;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingService;
import ru.practicum.server.booking.BookingState;
import ru.practicum.server.booking.Status;
import ru.practicum.server.comment.Comment;
import ru.practicum.server.comment.CommentRepository;
import ru.practicum.server.comment.CommentService;
import ru.practicum.server.exception.UserNotGiveItemException;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.catchThrowableOfType;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository repository;

    @MockBean
    private BookingService bookingService;

    @Test
    void testCreateComment() {
        //Given
        Comment comment = Mockito.mock(Comment.class);
        User user = Mockito.mock(User.class);

        Item item = Mockito.mock(Item.class);
        doReturn(1L).when(item).getId();

        Booking booking = Mockito.mock(Booking.class);
        Item itemBooking = Mockito.mock(Item.class);
        doReturn(List.of(booking)).when(bookingService).getAllBookingByUser(BookingState.PAST, user, 0, 10);
        doReturn(itemBooking).when(booking).getItem();
        doReturn(1L).when(itemBooking).getId();
        doReturn(Status.APPROVED).when(booking).getStatus();

        //When
        commentService.createComment(comment, user, item);

        //Then
        verify(repository, Mockito.times(1)).save(comment);

    }

    @Test
    void testCreateCommentFailed() {
        //Given
        Comment comment = Mockito.mock(Comment.class);
        User user = Mockito.mock(User.class);

        Item item = Mockito.mock(Item.class);
        doReturn(1L).when(item).getId();

        Booking booking = Mockito.mock(Booking.class);
        Item itemBooking = Mockito.mock(Item.class);
        doReturn(List.of(booking)).when(bookingService).getAllBookingByUser(BookingState.PAST, user, 0,10);
        doReturn(itemBooking).when(booking).getItem();
        doReturn(2L).when(itemBooking).getId();
        doReturn(Status.APPROVED).when(booking).getStatus();

        //When

        //Then
        catchThrowableOfType(
                () -> commentService.createComment(comment, user, item), UserNotGiveItemException.class
        );
        verify(repository, Mockito.times(0)).save(comment);

    }
}
