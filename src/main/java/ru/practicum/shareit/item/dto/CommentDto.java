package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String text;

    @NotBlank(message = "Имя автора комментария должно быть указано")
    private String authorName;
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(text, that.text) && Objects.equals(authorName, that.authorName) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, authorName, created);
    }
}
