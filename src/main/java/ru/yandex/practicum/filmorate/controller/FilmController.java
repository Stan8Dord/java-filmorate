package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private static int lastId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("post: \n" + film);
        if (validate(film) && !films.values().contains(film)) {
            film.setId(lastId);
            films.put(lastId++, film);
        } else {
            log.error("не пройдена валидация данных нового фильма \n" + film);
            throw new ValidationException("Некорректные данные фильма!");
        }

        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("post: \n" + film);

        if (films.keySet().contains(film.getId()))
            films.put(film.getId(), film);
        else
            throw new ValidationException("Некорректные данные!");

        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>();

        if (!films.isEmpty()) {
            for (Film film : films.values()) {
                filmList.add(film);
            }
        }
        System.out.println("Проверка get: " + filmList);

        return filmList;
    }

    private Boolean validate(Film film) {
        Boolean isUncorrect = film.getReleaseDate().isBefore(CINEMA_BIRTHDAY) || film.getDuration() <= 0;
        if (film.getDescription() != null)
            if (!isUncorrect)
                isUncorrect = film.getDescription().length() > 200;

        return !isUncorrect;
    }
}
