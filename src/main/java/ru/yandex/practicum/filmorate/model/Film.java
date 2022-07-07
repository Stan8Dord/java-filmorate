package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private final String name;
    private String description;
    @NotNull
    private LocalDate releaseDate;
    private long duration;
    private Set<Long> likes = new HashSet<>();

    public Film(@JsonProperty("name") String name, @JsonProperty("description") String description,
                @JsonProperty("releaseDate") LocalDate releaseDate, @JsonProperty("duration") long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
