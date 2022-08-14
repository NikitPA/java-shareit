package ru.practicum.shareit.item.serivce;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.exception.UserNotGiveItemException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

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
        doReturn(List.of(booking)).when(bookingService).getAllBookingByUser("PAST", user);
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
        doReturn(List.of(booking)).when(bookingService).getAllBookingByUser("PAST", user);
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
