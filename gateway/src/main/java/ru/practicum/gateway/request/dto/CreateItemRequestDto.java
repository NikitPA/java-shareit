package ru.practicum.gateway.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestDto {

    @NotNull
    @NotBlank
    private String description;

    private final LocalDateTime created = LocalDateTime.now();
}
