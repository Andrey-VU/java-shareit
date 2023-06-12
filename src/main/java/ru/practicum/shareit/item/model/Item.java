package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Владелец вещи должен быть указан!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    private User owner;

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
