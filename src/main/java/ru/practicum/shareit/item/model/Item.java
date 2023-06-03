package ru.practicum.shareit.item.model;
import lombok.Data;
import ru.practicum.shareit.user.model.User;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Column(name = "owner_id")
    private long ownerId;
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotBlank
    @Column(name = "description")
    private String description;
    @NotBlank
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "request_id")
    private long requestId;

}
