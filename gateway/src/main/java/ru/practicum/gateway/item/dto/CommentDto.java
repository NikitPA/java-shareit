package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CommentDto {

    private long id;
    private String authorName;
    private String text;
    private LocalDateTime created;

}
