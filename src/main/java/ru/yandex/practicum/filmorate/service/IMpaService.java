package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface IMpaService {
    List<MpaRating> getAllMpa();

    MpaRating getMpaById(int id);
}
