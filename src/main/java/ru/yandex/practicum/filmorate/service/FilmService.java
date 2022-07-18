package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService implements IFilmService {
    FilmStorage filmStorage;
    IUserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, IUserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public void addLike(Long filmId, Long userId) {
        userService.getUserById(userId);
        getFilmById(filmId);

        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        userService.getUserById(userId);
        getFilmById(filmId);

        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> findPopular(Integer count) {
        List<Film> popFilms = new ArrayList<>();

        if (count <= 0) {
            throw new ValidationException("Некорректное количество count = " + count);
        }

        for (Long id : filmStorage.getPopularFilms(count)) {
            popFilms.add(getFilmById(id));
        }

        return popFilms;
    }

    public Film createFilm(Film film) {
        validate(film);
        return filmStorage.createFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        if (getAllFilms().stream().noneMatch(f -> f.getId() == film.getId()))
            throw new FilmNotFoundException("Не найдено!");
        validate(film);

        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        if (filmStorage.getAllFilms().stream().anyMatch(f -> f.getId() == id)) {
            return filmStorage.getFilmById(id);
        } else {
            throw new FilmNotFoundException("Нет такого фильма: id = " + id);
        }
    }

    private void validate(Film film) {
        Boolean isCorrect = !film.getReleaseDate().isBefore(FilmController.CINEMA_BIRTHDAY) && film.getDuration() > 0;
        if (film.getDescription() != null)
            if (isCorrect)
                isCorrect = film.getDescription().length() <= 200;

        if (!isCorrect) {
            log.error("не пройдена валидация данных нового фильма \n" + film);
            throw new ValidationException("Некорректные данные фильма!");
        }
    }
}
