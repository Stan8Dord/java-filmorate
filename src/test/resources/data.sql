INSERT INTO "genres" ("genre") VALUES ('Комедия');
INSERT INTO "genres" ("genre") VALUES ('Драма');
INSERT INTO "genres" ("genre") VALUES ('Мультфильм');
INSERT INTO "genres" ("genre") VALUES ('Триллер');
INSERT INTO "genres" ("genre") VALUES ('Документальный');
INSERT INTO "genres" ("genre") VALUES ('Боевик');


INSERT INTO "mpa_ratings"  ("mpa_rating") VALUES ('G');
INSERT INTO "mpa_ratings"  ("mpa_rating") VALUES ('PG');
INSERT INTO "mpa_ratings"  ("mpa_rating") VALUES ('PG-13');
INSERT INTO "mpa_ratings"  ("mpa_rating") VALUES ('R');
INSERT INTO "mpa_ratings"  ("mpa_rating") VALUES ('NC-17');


INSERT INTO "users" ("email", "login", "name", "birthday") VALUES ('mail@mail.ru', 'dolore', 'Nick', '1946-08-20');
INSERT INTO "users" ("email", "login", "name", "birthday") VALUES ('mail@mail2.ru', 'dolore2', 'Nick', '1944-08-20');
INSERT INTO "users" ("email", "login", "name", "birthday") VALUES ('mail@mail3.ru', 'dolore3', 'Nick', '1947-08-20');

INSERT INTO "friends" ("friend1_id", "friend2_id") VALUES (1, 3);
INSERT INTO "friends" ("friend1_id", "friend2_id") VALUES (2, 3);

INSERT INTO "films" ("name", "description" , "release_date", "duration", "rate", "mpa_rating_id")
	VALUES ('film1', 'New film', '1989-04-17', 120, 3, 5);
INSERT INTO "films" ("name", "description" , "release_date", "duration", "rate", "mpa_rating_id")
	VALUES ('film2', 'Old film', '1969-04-17', 110, 5, 3);

INSERT INTO "film_genres"  ("film_id" , "genre_id") VALUES (2, 1);
INSERT INTO "film_genres"  ("film_id" , "genre_id") VALUES (2, 2);
INSERT INTO "film_genres"  ("film_id" , "genre_id") VALUES (1, 5);

INSERT INTO "likes"  ("film_id"  , "user_id") VALUES (2, 1);
INSERT INTO "likes"  ("film_id"  , "user_id") VALUES (2, 3);
INSERT INTO "likes"  ("film_id"  , "user_id") VALUES (1, 2);

