package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test
	public void testGetUserById() {

		Optional<User> userOptional = Optional.of(userStorage.getUserById(1L));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}

	@Test
	public void testGetAllUsers() {

		List<User> allUsers = userStorage.getAllUsers();

		assertThat(allUsers).hasOnlyElementsOfType(User.class)
				.hasSize(3)
				.allSatisfy(u -> u.getName().equals("Nick"));
	}

	@Test
	public void testCreateUser() {

		User user = new User("testCreate@user.ru", "tester", "Anton",
				LocalDate.of(2000, 01, 01));
		Optional<User> userOptional = Optional.of(userStorage.createUser(user));

		assertThat(userOptional).isPresent()
				.hasValueSatisfying(u -> {
						assertThat(u).hasFieldOrPropertyWithValue("login", "tester");
						assertThat(u).hasFieldOrPropertyWithValue("id", 4L);}
				);
	}

	@Test
	public void testUpdateUser() {

		User user = userStorage.getUserById(1L);
		user.setLogin("karton");
		userStorage.updateUser(user);

		Optional<User> userOptional = Optional.of(userStorage.getUserById(1L));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(u ->
						assertThat(u).hasFieldOrPropertyWithValue("login", "karton")
				);
	}

	@Test
	public void testGetUserFriends() {

		List<Long> friends = userStorage.getUserFriends(1L);

		assertThat(friends).hasSize(1);
	}

	@Test
	public void testAddAndRemoveFriend() {

		userStorage.addFriend(1L, 2L);
		List<Long> friends = userStorage.getUserFriends(1L);

		assertThat(friends).hasSize(2)
				.contains(2L, 3L);

		userStorage.removeFriend(1L, 3L);
		friends = userStorage.getUserFriends(1L);

		assertThat(friends).hasSize(1)
				.contains(2L);
	}

	@Test
	public void testGetCommonFriends() {

		List<Long> commonFriends = userStorage.getCommonFriends(1L, 2L);

		assertThat(commonFriends).hasSize(1)
				.containsExactly(3L);
	}

	@Test
	public void testGetFilmById() {

		Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(1L));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("name", "film1")
				);
	}

	@Test
	public void testGetAllFilms() {

		List<Film> allFilms = filmStorage.getAllFilms();

		assertThat(allFilms).hasOnlyElementsOfType(Film.class)
				.hasSize(2)
				.allSatisfy(f -> f.getName().contains("film"));
	}

	@Test
	public void testCreateFilm() {

		Film film = new Film("film3", "serial", LocalDate.of(2000, 01, 01),
				1110L, 6, new MpaRating(3), null);
		Optional<Film> filmOptional = Optional.of(filmStorage.createFilm(film));

		assertThat(filmOptional).isPresent()
				.hasValueSatisfying(f -> {
					assertThat(f).hasFieldOrPropertyWithValue("description", "serial");
					assertThat(f).hasFieldOrPropertyWithValue("id", 3L);}
				);
	}

	@Test
	public void testUpdateFilm() {

		Film film = filmStorage.getFilmById(2L);
		film.setDescription("SuperHit");
		filmStorage.updateFilm(film);

		Optional<Film> filmOptional = Optional.of(filmStorage.getFilmById(2L));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(f ->
						assertThat(f).hasFieldOrPropertyWithValue("description", "SuperHit")
				);
	}

	@Test
	public void testGetPopularFilms() {
		List<Long> films = filmStorage.getPopularFilms(1);

		assertThat(films).hasSize(1)
				.containsExactly(2L);
	}

	@Test
	public void testAddRemoveLike() {

		filmStorage.addLike(1L, 1L);
		filmStorage.removeLike(2L, 3L);

		List<Long> films = filmStorage.getPopularFilms(1);

		assertThat(films).hasSize(1)
				.containsExactly(1L);
	}

	@Test
	public void testRemoveGenres() {
		Film film = filmStorage.getFilmById(2L);
		assertThat(film.getGenres().size() == 2);

		filmStorage.removeGenres(2L);

		film = filmStorage.getFilmById(2L);
		assertThat(film.getGenres().size() == 0);
	}
}
