package ru.practicum.server.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.booking.BookingService;
import ru.practicum.server.booking.BookingState;
import ru.practicum.server.booking.Status;
import ru.practicum.server.exception.UserNotGiveItemException;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookingService bookingService;


    @Override
    public Comment createComment(Comment comment, User user, Item item) {
        boolean match = bookingService.getAllBookingByUser(BookingState.PAST, user, 0, 10)
                .stream()
                .anyMatch(booking -> booking.getItem().getId().equals(item.getId())
                        && booking.getStatus() == Status.APPROVED);
        if (!match) {
            throw new UserNotGiveItemException(user.getId(), item.getId());
        }
        return commentRepository.save(comment);
    }
}
