package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreServiceInterface {
    List<Genre> getAllGenres();

    Genre getGenreById(int id);
}
