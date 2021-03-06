package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService implements IMpaService {
    @Qualifier("mpaDbStorage")
    MpaStorage storage;

    @Autowired
    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<MpaRating> getAllMpa() {
        return storage.getAllMpa();
    }

    @Override
    public MpaRating getMpaById(int id) {
        if (storage.getAllMpa().stream().anyMatch(m -> m.getId() == id))
            return storage.getMpaById(id);
        else
            throw new NotFoundException("Нет рейтинга фильмов с id = " + id);
    }
}
