package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

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
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
    }

    @Override
    public List<Long> getPopularFilms(int count){
        return null;
    }

    @Override
    public void addLike(Long filmId, Long userId){}

    @Override
    public void removeLike(Long filmId, Long userId){}
}
