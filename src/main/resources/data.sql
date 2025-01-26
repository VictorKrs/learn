INSERT INTO author (second_name, first_name, middle_name) VALUES ('Булгаков', 'Михаил', 'Афанасьевич');
INSERT INTO author (second_name, first_name, middle_name) VALUES ('Ремарк', 'Эрих Мария', '');
INSERT INTO author (second_name, first_name, middle_name) VALUES ('Достоевский', 'Федор', 'Михайлович');
INSERT INTO author (second_name, first_name, middle_name) VALUES ('Оруэлл', 'Джордж', '');
INSERT INTO author (second_name, first_name, middle_name) VALUES ('Уайльд', 'Оскар', '');
INSERT INTO author (second_name, first_name, middle_name) VALUES ('Толстой', 'Лев', 'Николаевич');
INSERT INTO author (second_name, first_name, middle_name) VALUES ('Гоголь', 'Николай', 'Васильевич');

INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

INSERT INTO users (username, password) VALUES ('admin', '$2a$10$WsEbkPytgRr.HiVCa7djmOiu0Mwz3VcWPk4M/0.2s2/t1nztI27Wq');
INSERT INTO users (username, password) VALUES ('user', '$2a$10$WsEbkPytgRr.HiVCa7djmOiu0Mwz3VcWPk4M/0.2s2/t1nztI27Wq');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);
