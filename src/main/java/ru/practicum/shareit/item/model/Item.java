package ru.practicum.shareit.item.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Владелец вещи должен быть указан!")
    @Positive
    @Column(name = "owner_id")
    private long ownerId;

    @NotBlank(message = "Название вещи не может быть пустым!")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым!")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Статус вещи не может быть пустым!")
    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "request_id")
    private long requestId;

}
