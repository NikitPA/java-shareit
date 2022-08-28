package ru.practicum.server.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestDto {

    private String description;
    private final LocalDateTime created = LocalDateTime.now();
}
