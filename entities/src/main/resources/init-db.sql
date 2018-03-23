-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

-- user roles
INSERT INTO user_role (name) VALUES ('Administrator'), ('Student'), ('Professor');

-- user logins
INSERT INTO user_login (username, password, id_user_role, salt) VALUES ('joze1', '263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62', 1, 123);

-- study years
INSERT INTO study_year (name) VALUES ('2016/2017'), ('2017/2018');

-- study programs
INSERT INTO study_program (name) VALUES ('Računalništvo in informatika (UNI)'), ('Računalništvo in informatika (VSŠ)'), ('Računalništvo in matematika');

-- student
INSERT INTO student (register_number, name, surname, email, id_login, id_study_program, id_study_year) VALUES ('63180001', 'Janez', 'Novak', 'janez.novak@gmail.com', 1, 1, 1), ('63180002', 'Miha', 'Kopač', 'miha21kopac@gmail.com', 1, 2, 1), ('63180003', 'Marta', 'Veljak', 'marta.veljak0000@gmail.com', 1, 3, 2);