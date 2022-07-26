package ru.practicum.shareit.comment;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getAuthor().getName(),
                comment.getText(),
                comment.getCreated());
    }

    public static Comment toComment(CreateCommentDto createCommentDto, User user, Item item) {
        return new Comment(
                null,
                createCommentDto.getText(),
                user,
                item,
                LocalDateTime.now()
        );
    }
}
