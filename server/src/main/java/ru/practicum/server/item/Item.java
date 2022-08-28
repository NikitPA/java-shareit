package ru.practicum.server.item;

import lombok.*;
import ru.practicum.server.comment.Comment;
import ru.practicum.server.request.ItemRequest;
import ru.practicum.server.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * // TODO .
 */
@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    @OneToMany(mappedBy = "item")
    private final Collection<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Include
    private User owner;

    @ManyToOne
    private ItemRequest request;
}
