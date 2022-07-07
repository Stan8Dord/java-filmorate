package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private static UserController userController;
    User dummyUser;

    @BeforeAll
    public static void beforeAll() {
        InMemoryUserStorage storage = new InMemoryUserStorage();
        userController = new UserController(storage, new UserService(storage));
    }

    @BeforeEach
    public void beforeEach() {
        dummyUser = new User("email@email.ru", "login", "name", LocalDate.of(2000, 1, 1));
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

        Long id = userController.createUser(dummyUser).getId();

        assertTrue(userController.getAllUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst().get().getName() == dummyUser.getLogin());
    }
}
