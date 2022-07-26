package ru.practicum.shareit.comment;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public interface CommentService {

    Comment createComment(Comment comment, User user, Item item);
}
