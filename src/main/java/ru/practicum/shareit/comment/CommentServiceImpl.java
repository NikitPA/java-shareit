package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.UserNotGiveItemException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookingService bookingService;


    @Override
    public Comment createComment(Comment comment, User user, Item item) {
        boolean match = bookingService.getAllBookingByUser("PAST", user)
                .stream()
                .anyMatch(booking -> booking.getItem().getId() == item.getId()
                        && booking.getStatus() == Status.APPROVED);
        if (!match) {
            throw new UserNotGiveItemException(user.getId(), item.getId());
        }
        return commentRepository.save(comment);
    }
}
