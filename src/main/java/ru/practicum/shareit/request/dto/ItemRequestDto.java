package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter @Setter
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Запрос не возможно создать без описания")
    private String description;
    private Long requesterId;
    private LocalDateTime created;


    @Override
    public String toString() {
        return "ItemRequestDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", requesterId=" + requesterId +
                ", created=" + created +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRequestDto)) return false;
        ItemRequestDto that = (ItemRequestDto) o;
        return Objects.equals(description, that.description) && Objects.equals(requesterId, that.requesterId) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, requesterId, created);
    }
}
