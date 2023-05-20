package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Item {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private Boolean available;
}
