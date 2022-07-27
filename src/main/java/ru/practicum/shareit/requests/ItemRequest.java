package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@Entity
@Table(name = "item_request")
@NoArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String description;
    private long requesterId;
    @NotNull
    private LocalDateTime created;
}
