package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> findPopular(Integer count);

    Film createFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    Film getFilmById(Long id);
}
