package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data @AllArgsConstructor
public class Film {
    private int id;
    @NotNull @NotBlank
    private final String name;
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private long duration;
}
