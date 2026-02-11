DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (id, date_time, description, calories, user_id)
VALUES (1, '2020-01-30T10:00', 'Завтрак', 500, 100000)
     , (2, '2020-01-30T13:00', 'Обед', 1000, 100000)
     , (3, '2020-01-30T20:00', 'Ужин', 500, 100000)
     , (4, '2020-01-31T00:00', 'Еда на граничное значение', 100, 100000)
     , (5, '2020-01-31T10:00', 'Завтрак', 1000, 100000)
     , (6, '2020-01-31T13:00', 'Обед', 500, 100000)
     , (7, '2020-01-31T20:00', 'Ужин', 410, 100000)
     , (8, '2015-06-01T14:00', 'Админ ланч', 510, 100001)
     , (9, '2015-06-01T21:00', 'Админ ужин', 1500, 100001);