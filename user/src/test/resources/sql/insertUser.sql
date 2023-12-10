INSERT INTO user (id, email, nick_name, password)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e8"), 'andrzej.piaseczny@gmail.com','Anda',
        '$2a$10$08qGliNU4tiVu9IIy8Kol.0kD9VXCzv9xkLcKCs9Q4hACgdc43B0O');

INSERT INTO user_role (user_id, role_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e8"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5a9"));

