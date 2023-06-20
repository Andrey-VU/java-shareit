package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@ToString
@Getter @Setter
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Запрос не возможно создать без описания")
    private String description;
    private Long requesterId;
    private LocalDateTime created;
}
