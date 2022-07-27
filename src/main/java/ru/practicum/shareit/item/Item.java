package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "Имя не может быть пустым.")
    @NotNull(message = "Имя должно передаваться параметром.")
    private String name;

    @NotBlank(message = "Описание не может быть пустым.")
    @NotNull(message = "Описание должно передаваться параметром.")
    private String description;

    @NotNull(message = "Нужно указать доступна вещь или нет.")
    private Boolean available;

    @OneToMany(mappedBy = "item")
    private final Collection<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @EqualsAndHashCode.Include
    private User owner;

    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest request;
}
