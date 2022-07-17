package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaServiceInterface {
    List<MpaRating> getAllMpa();

    MpaRating getMpaById(int id);
}
