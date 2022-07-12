package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
public class FilmController {
    private final FilmService service;
    public static final Logger log = LoggerFactory.getLogger(FilmController.class);
    public static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("post: \n" + film);

        return service.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("post: \n" + film);

        return service.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/films/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10") Integer count) {
        return service.findPopular(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") Long filmId) {
        return service.getFilmById(filmId);
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
