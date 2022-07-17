package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceInterface;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceInterface;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private static FilmController filmController;
    Film dummyFilm;

    @BeforeAll
    public static void beforeAll() {
        UserServiceInterface userService = new UserService(new InMemoryUserStorage());
        FilmServiceInterface filmService = new FilmService(new InMemoryFilmStorage(), userService);
        filmController = new FilmController(filmService);
    }

    @BeforeEach
    public void beforeEach() {
        dummyFilm = new Film("SuperFilm", "Hit", LocalDate.of(2000, 1, 1), 100, 5, null, null);
    }

    @Test
    public void shouldFailValidationWithOldRelease() {
        String text = "";
        dummyFilm.setReleaseDate(LocalDate.of(1000, 1, 1));

        try {
            filmController.createFilm(dummyFilm);
        } catch (Exception e) {
            text = e.getMessage();
        }
        assertEquals("Некорректные данные фильма!", text);
        assertThrows(RuntimeException.class, () -> filmController.createFilm(dummyFilm));
    }

    @Test
    public void shouldValidateWithCinemaBirthday() {
        String text = "нет ошибки";
        dummyFilm.setReleaseDate(FilmController.CINEMA_BIRTHDAY);

        try {
            filmController.createFilm(dummyFilm);
        } catch (Exception e) {
            text = e.getMessage();
        }
        assertEquals("нет ошибки", text);
        assertEquals(filmController.getAllFilms().size(), 1);
    }

    @Test
    public void shouldFailValidationOfNegativeDuration() {
        String text = "";
        dummyFilm.setDuration(-1);

        try {
            filmController.createFilm(dummyFilm);
        } catch (Exception e) {
            text = e.getMessage();
        }

        assertEquals(text, "Некорректные данные фильма!");
        assertThrows(ValidationException.class, () -> filmController.createFilm(dummyFilm));
    }

    @Test
    public void shouldFailValidationOfZeroDuration() {
        String text = "";
        dummyFilm.setDuration(0);

        try {
            filmController.createFilm(dummyFilm);
        } catch (Exception e) {
            text = e.getMessage();
        }

        assertEquals(text, "Некорректные данные фильма!");
        assertThrows(ValidationException.class, () -> filmController.createFilm(dummyFilm));
    }

    @Test
    public void shouldFailValidationWithLongDescription() {
        String text = "";
        dummyFilm.setDescription("Текст длиной больше 200 символов! Текст длиной больше 200 символов! Текст длиной " +
                "больше 200 символов! Текст длиной больше 200 символов! Текст длиной больше 200 символов! " +
                "Текст длиной больше 200 символов! ");

        try {
            filmController.createFilm(dummyFilm);
        } catch (Exception e) {
            text = e.getMessage();
        }

        assertTrue(text.equals("Некорректные данные фильма!"));
        assertThrows(ValidationException.class, () -> filmController.createFilm(dummyFilm));
    }

    @Test
    public void shouldValidate200LengthDescription() {
        String text = "ошибки нет";
        dummyFilm.setDescription("Текст длиной 200 символов! Текст длиной 200 символов! Текст длиной 200 символов! " +
                "Текст длиной 200 символов!Текст длиной 200 символов! Текст длиной 200 символов!" +
                "Текст длиной 200 символов! Текст длиной!");

        try {
            filmController.createFilm(dummyFilm);
        } catch (Exception e) {
            text = e.getMessage();
        }

        assertEquals(dummyFilm.getDescription().length(), 200);
        assertTrue(text.equals("ошибки нет"));
        assertDoesNotThrow(() -> filmController.createFilm(dummyFilm));
    }
}
