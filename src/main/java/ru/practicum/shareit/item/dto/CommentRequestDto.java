package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CommentRequestDto {
    private Long authorId;
    private Long itemId;

    @NotBlank(message = "Комментарий невозможно создать без описания")
    private String text;
}
