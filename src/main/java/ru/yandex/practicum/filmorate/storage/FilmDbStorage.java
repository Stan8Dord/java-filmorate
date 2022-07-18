package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("dbFStorage")
@Primary
public class FilmDbStorage implements  FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    MpaStorage mpaStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "insert into \"films\"(\"name\", \"description\", \"release_date\", \"duration\", " +
                "\"rate\", \"mpa_rating_id\") " + "values (?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement prst =
                    connection.prepareStatement(sql, new String[]{"id"});
            prst.setString(1, film.getName());
            prst.setString(2, film.getDescription());
            prst.setDate(3, Date.valueOf(film.getReleaseDate()));
            prst.setLong(4, film.getDuration());
            prst.setLong(5, film.getRate());
            prst.setInt(6, film.getMpa().getId());
            return prst;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQ = "insert into \"film_genres\"(\"film_id\", \"genre_id\") " + "values (?, ?)";
                jdbcTemplate.update(sqlQ, film.getId(), genre.getId());
            }
        }

        film.getMpa().setName(mpaStorage.getMpaById(film.getMpa().getId()).getName());

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select \"id\" from \"films\"");

        while(filmRows.next()) {
            long id = filmRows.getLong("id");

            allFilms.add(getFilmById(id));
        }
        return allFilms;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update \"films\" set " + "\"name\" = ?, \"description\" = ?, \"release_date\" = ?" +
                ", \"duration\" = ?, \"rate\" = ?, \"mpa_rating_id\" = ?" + "where \"id\" = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        removeGenres(film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQ = "insert into \"film_genres\"(\"film_id\", \"genre_id\") " + "values (?, ?)";
                jdbcTemplate.update(sqlQ, film.getId(), genre.getId());
            }
        }

        film.getMpa().setName(mpaStorage.getMpaById(film.getMpa().getId()).getName());

        return film;
    }


    @Override
    public Film getFilmById(Long id) {
        Film film = null;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from \"films\" where \"id\" = ?", id);

        if(filmRows.next()) {
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select \"user_id\" from \"likes\" where \"film_id\" = ?", id);
            Set<Long> filmLikes = new HashSet<>();

            while (likesRows.next()) {
                filmLikes.add(likesRows.getLong("user_id"));
            }

            SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select f.\"genre_id\", g.\"genre\" " +
                    "from \"film_genres\" f join \"genres\" g on f.\"genre_id\" = g.\"id\" where f.\"film_id\" = ?", id);
            Set<Genre> filmGenres = new HashSet<>();

            while (genresRows.next()) {
                Genre genre = new Genre(genresRows.getInt("genre_id"), genresRows.getString("genre"));
                filmGenres.add(genre);
            }
            film = new Film(
                    id,
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getLong("duration"),
                    filmRows.getInt("rate"),
                    mpaStorage.getMpaById(filmRows.getInt("mpa_rating_id")),
                    filmLikes,
                    filmGenres
            );
        }

        return film;
    }

    @Override
    public List<Long> getPopularFilms(int count) {
        List<Long> films = new ArrayList<>();

        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT f.\"id\", count(l.\"user_id\") " +
                "FROM \"films\" f JOIN \"likes\" l ON f.\"id\"  = l.\"film_id\" \n" +
                "GROUP BY f.\"id\" " +
                "ORDER BY count(l.\"user_id\") desc LIMIT ?", count);

        while (filmsRows.next()) {
            films.add(filmsRows.getLong("id"));
        }

        if (films.isEmpty()) {
            filmsRows = jdbcTemplate.queryForRowSet("SELECT f.\"id\"" +
                    "FROM \"films\" f LIMIT ?", count);
            while (filmsRows.next()) {
                films.add(filmsRows.getLong("id"));
            }
        }

        System.out.println("films = " + films.toString());
        return films;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "insert into \"likes\"(\"film_id\", \"user_id\") " + "values (?, ?)";

        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sqlQuery = "delete from \"likes\" where \"film_id\" = ? and \"user_id\" = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void removeGenres(Long film_id) {
        String sqlQuery = "delete from \"film_genres\" where \"film_id\" = ?";

        jdbcTemplate.update(sqlQuery, film_id);
    }
}
