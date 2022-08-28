package ru.practicum.server.comment;

import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

public interface CommentService {

    Comment createComment(Comment comment, User user, Item item);
}
