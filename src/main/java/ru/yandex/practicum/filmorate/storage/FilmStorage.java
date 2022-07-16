package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film createFilm(Film film);

    public List<Film> getAllFilms();

    public Film updateFilm(Film film);

    public Film getFilmById(Long id);

    public List<Long> getPopularFilms(int count);

    public void addLike(Long filmId, Long userId);

    public void removeLike(Long filmId, Long userId);
}
