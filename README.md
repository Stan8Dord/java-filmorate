# java-filmorate
Template repository for Filmorate project.

![Схема БД](https://github.com/Stan8Dord/java-filmorate/blob/main/DB_filmorate_v1.png)

Выделены таблицы 
users, films - основные таблицы модели  
mpa_ratings, genres - дополнительные справочники рейтингов и жанров для фильмов  
friends, likes, film_genres - таблицы связи для отношнеий n к m  
  
Примеры запросов:   
  
Получение всех фильмов:  
```
SELECT f.id,
       f.name,
       f.description,
       f.release_date,
       f.duration,
       m.mpa_rating,
       l.user_id AS liked_user_id
FROM films AS f
LEFT JOIN mpa_rating AS m ON f.mpa_rating_id = m.id
LEFT JOIN likes AS l ON l.film_id = f.id
```
Получение всех пользователей:
```
SELECT u.id,
       u.email,
       u.login,
       u.name,
       u.birthday,
       f.friend2_id,
       f.friendship_status
FROM users AS u
LEFT JOIN friends AS f ON f.friend1_id = u.id
```
Топ 10 наиболее популярных фильмов:
```
SELECT f.id
FROM
  (SELECT f.id,
          count(user_id)
   FROM films AS f
   LEFT JOIN likes AS l ON f.id = l.film_id
   GROUP BY f.id
   ORDER BY count(user_id) DESC
   LIMIT 10)
```   
Список общих друзей пользователей 1 и 2:
```
SELECT q1.friend2_id AS common_friends
FROM
  (SELECT f.friend2_id
   FROM users AS u
   LEFT JOIN friends AS f ON u.id = f.friend1_id
   WHERE u.id = 1 ) AS q1
JOIN
  (SELECT f.friend2_id
   FROM users AS u
   LEFT JOIN friends AS f ON u.id = f.friend1_id
   WHERE u.id = 2 ) AS q2 ON q1.friend2_id = q2.friend2_id
```   
