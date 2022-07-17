package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MpaRating {
    private int id;
    private String name;

    public MpaRating() {}

    public MpaRating(@JsonProperty("id") int id) {
        this.id = id;
        this.name = "";
    }
}
