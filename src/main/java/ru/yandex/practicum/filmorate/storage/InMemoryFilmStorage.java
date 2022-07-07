package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static Long lastId = 1L;

    @Override
    public Film createFilm(Film film) {
        film.setId(lastId);
        films.put(lastId++, film);

        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        if (films.containsKey(id)) {
            return Optional.of(films.get(id));
        }

        return Optional.empty();
    }
}
