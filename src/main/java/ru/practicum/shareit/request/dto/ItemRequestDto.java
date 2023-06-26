package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Запрос не возможно создать без описания")
    private String description;
    private Long requesterId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    private List<ItemDto> items = new ArrayList<>();

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
