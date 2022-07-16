package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaStorage {
    public List<MpaRating> getAllMpa();

    public MpaRating getMpaById(int id);
}
