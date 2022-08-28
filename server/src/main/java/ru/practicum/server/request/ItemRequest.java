package ru.practicum.server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * // TODO .
 */
@Data
@Entity
@Table(name = "item_request")
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDateTime created;

    @ManyToOne
    private User requestor;

    @OneToMany(mappedBy = "request")
    private final Collection<Item> items = new ArrayList<>();
}
