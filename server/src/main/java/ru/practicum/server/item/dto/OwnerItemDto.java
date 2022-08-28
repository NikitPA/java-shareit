package ru.practicum.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.practicum.server.comment.CommentDto;

import java.util.Collection;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class OwnerItemDto {

    private final Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private Collection<CommentDto> comments;
    private Booking lastBooking;
    private Booking nextBooking;

    @AllArgsConstructor
    @Getter
    public static class Booking {
        private Long id;

        private Long bookerId;
    }
}
