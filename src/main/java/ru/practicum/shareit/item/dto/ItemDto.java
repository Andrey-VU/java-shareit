package ru.practicum.shareit.item.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
//    @NotBlank
//    private long owner;
}
