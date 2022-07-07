package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public Film createFilm(Film film);

    public Collection<Film> getAllFilms();

    public Film updateFilm(Film film);

    public Optional<Film> getFilmById(Long id);
}
