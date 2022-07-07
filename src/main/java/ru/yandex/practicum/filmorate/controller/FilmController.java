package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
public class FilmController {
    private final FilmStorage storage;
    private final FilmService service;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
        this.storage = service.getFilmStorage();
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("post: \n" + film);

        if (validate(film)) {
            storage.createFilm(film);
        } else {
            log.error("не пройдена валидация данных нового фильма \n" + film);
            throw new ValidationException("Некорректные данные фильма!");
        }

        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("post: \n" + film);

        if (storage.getAllFilms().stream().filter(f -> f.getId() == film.getId()).findFirst().isEmpty())
            throw new FilmNotFoundException("Не найдено!");
        else
            return storage.updateFilm(film);
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    private Boolean validate(Film film) {
        Boolean isCorrect = !film.getReleaseDate().isBefore(CINEMA_BIRTHDAY) && film.getDuration() > 0;
        if (film.getDescription() != null)
            if (isCorrect)
                isCorrect = film.getDescription().length() <= 200;

        return isCorrect;
    }

    @GetMapping("/films/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new ValidationException("Некорректное количество count = " + count);
        }

        return service.findPopular(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") Long filmId) {
        Optional<Film> filmOptional = storage.getFilmById(filmId);

        if (filmOptional.isEmpty()) {
            throw new FilmNotFoundException("Нет такого фильма: id = " + filmId);
        }

        return filmOptional.get();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        service.removeLike(filmId, userId);
    }
}
