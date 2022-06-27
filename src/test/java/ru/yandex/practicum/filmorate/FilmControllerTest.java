package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {
    private final FilmController filmController = new FilmController();
    Film dummyFilm;

    @BeforeEach
    public void beforeEach() {
        dummyFilm = new Film(1, "SuperFilm", "Hit", LocalDate.of(2000, 1, 1), 100);
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
    }

    @Test
    public void shouldValidateWithCinemaBirthday() {
        String text = "нет ошибки";
        LocalDate now = LocalDate.now();
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
        assertEquals(filmController.getAllFilms().size(), 1);
    }
}
