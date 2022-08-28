package ru.practicum.server.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<Item> items;

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Item {

        private long id;

        private String name;

        private String description;

        private boolean available;

        private long requestId;
    }
}
