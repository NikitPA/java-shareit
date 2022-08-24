package ru.practicum.server.item;

import org.springframework.stereotype.Component;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.comment.CommentMapper;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.OwnerItemDto;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.user.User;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getComments().stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList())
        );
    }

    public static OwnerItemDto toOwnerItemDto(Item item, Booking nextBooking, Booking lastBooking) {
        return new OwnerItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getComments().stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()),
                lastBooking != null ? new OwnerItemDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId())
                        : null,
                nextBooking != null ? new OwnerItemDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId())
                        : null
        );
    }

    public static Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user,
                itemRequest
        );
    }

    public static Item patchUser(Item itemById, Map<Object, Object> updateFields) {
        return new Item(
                itemById.getId(),
                checkName(itemById, updateFields),
                checkDescription(itemById, updateFields),
                checkAvailable(itemById, updateFields),
                itemById.getOwner(),
                itemById.getRequest()
        );
    }

    private static String checkName(Item item, Map<Object, Object> updateFields) {
        if (updateFields.containsKey("name")) {
            return updateFields.get("name").toString();
        } else {
            return item.getName();
        }
    }

    private static String checkDescription(Item item, Map<Object, Object> updateFields) {
        if (updateFields.containsKey("description")) {
            return updateFields.get("description").toString();
        } else {
            return item.getDescription();
        }
    }

    private static Boolean checkAvailable(Item item, Map<Object, Object> updateFields) {
        if (updateFields.containsKey("available")) {
            return Boolean.valueOf(updateFields.get("available").toString());
        } else {
            return item.getAvailable();
        }
    }
}
