package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoGateway {
    private Long id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}
