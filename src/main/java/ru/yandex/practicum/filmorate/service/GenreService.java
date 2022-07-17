package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService implements GenreServiceInterface {
    @Autowired
    @Qualifier("genreDbStorage")
    GenreStorage storage;

    @Autowired
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    @Override
    public Genre getGenreById(int id) {
        if (storage.getAllGenres().stream().anyMatch(g -> g.getId() == id))
            return storage.getGenreById(id);
        else
            throw new NotFoundException("Нет жанра с id = " + id);
    }
}
