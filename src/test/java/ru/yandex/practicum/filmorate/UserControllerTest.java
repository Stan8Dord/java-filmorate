package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private final UserController userController = new UserController();
    User dummyUser;

    @BeforeEach
    public void beforeEach() {
        dummyUser = new User(1, "email@email.ru", "login", "name", LocalDate.of(2000, 1, 1));
    }

    @Test
    public void shouldFailValidationWithFutureBirthday() {
        String text = "";
        dummyUser.setBirthday(LocalDate.of(3000, 1, 1));

        try {
            userController.createUser(dummyUser);
        } catch (Exception e) {
            text = e.getMessage();
        }
        assertEquals("Некорректные данные нового пользователя!", text);
        assertThrows(ValidationException.class, () -> userController.createUser(dummyUser));
    }

    @Test
    public void shouldValidateWithNowBirthday() {
        String text = "";
        LocalDate now = LocalDate.now();
        dummyUser.setBirthday(now);

        try {
            userController.createUser(dummyUser);
        } catch (Exception e) {
            text = e.getMessage();
        }
        assertEquals("", text);
        assertEquals(userController.getAllUsers().size(), 1);
        assertDoesNotThrow(() -> userController.createUser(dummyUser));
    }

    @Test
    public void shouldFillEmptyNameFromLogin() {
        dummyUser.setName("");

        userController.createUser(dummyUser);

        assertTrue(userController.getAllUsers().stream().findFirst().get().getName() == dummyUser.getLogin());
    }

}
