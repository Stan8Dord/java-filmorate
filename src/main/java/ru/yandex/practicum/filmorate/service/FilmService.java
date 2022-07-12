package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public void addLike(Long filmId, Long userId) {
        userService.getUserById(userId);
        getFilmById(filmId);

        Film film = filmStorage.getFilmById(filmId);

        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        userService.getUserById(userId);
        getFilmById(filmId);

        Film film = filmStorage.getFilmById(filmId);

        film.getLikes().remove(userId);
    }

    public List<Film> findPopular(Integer count) {
        if (count <= 0) {
            throw new ValidationException("Некорректное количество count = " + count);
        }
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
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
            FilmController.log.error("не пройдена валидация данных нового фильма \n" + film);
            throw new ValidationException("Некорректные данные фильма!");
        }
    }
}
