package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.IGenreService;

import java.util.List;

@RestController
public class GenreController {
    private final IGenreService service;

    @Autowired
    public GenreController(IGenreService service) {
        this.service = service;
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable("id") int id) {
        return service.getGenreById(id);
    }
}
