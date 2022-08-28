package ru.practicum.server.comment;

import lombok.*;
import ru.practicum.server.item.Item;
import ru.practicum.server.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    private User author;

    @ManyToOne
    private Item item;

    private LocalDateTime created;
}
