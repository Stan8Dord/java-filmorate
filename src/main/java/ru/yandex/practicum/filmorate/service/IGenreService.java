package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface IGenreService {
    List<Genre> getAllGenres();

    Genre getGenreById(int id);
}
