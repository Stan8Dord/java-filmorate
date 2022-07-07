package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Getter
    FilmStorage filmStorage;
    UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    private void checkFilmById(Long filmId) {
        Optional<Film> filmOptional = filmStorage.getFilmById(filmId);
        if (filmOptional.isEmpty()) {
            throw new FilmNotFoundException("Нет такого фильма: id = " + filmId);
        }
    }

    public void addLike(Long filmId, Long userId) {
        userService.checkUserById(userId);
        checkFilmById(filmId);

        Film film = filmStorage.getFilmById(filmId).get();

        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        userService.checkUserById(userId);
        checkFilmById(filmId);

        Film film = filmStorage.getFilmById(filmId).get();

        film.getLikes().remove(userId);
    }

    public List<Film> findPopular(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
