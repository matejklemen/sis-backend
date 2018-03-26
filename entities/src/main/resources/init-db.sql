-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

-- user roles
INSERT INTO user_role (name) VALUES ('Administrator'), ('Student'), ('Professor');

-- user logins
INSERT INTO user_login (username, password, id_user_role, salt) VALUES ('joze1', '263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62', 1, 123);

-- study years
INSERT INTO study_year (name) VALUES ('2016/2017'), ('2017/2018');

-- study programs
INSERT INTO study_program (name) VALUES ('Računalništvo in informatika (UNI)'), ('Računalništvo in informatika (VSŠ)'), ('Računalništvo in matematika');

-- address
INSERT INTO post_address(id, name) VALUES (1, 'Moja dežela');
INSERT INTO address (line1, line2, id_post_address, country) VALUES ('Kul kraj 15', 'Druga vrstica naslova', 1, 'Slovenija'), ('Nekje 26', '', 1, 'Slovenija');

-- student
INSERT INTO student (register_number, name, surname, id_address1, id_address2, phone_number, email, id_login) VALUES ('63180001', 'Janez', 'Novak', 1, 2, '+386 666 666', 'janez.novak@gmail.com', 1);
INSERT INTO student (register_number, name, surname, id_address1, email, id_login) VALUES ('63180002', 'Miha', 'Kopač', 1, 'miha21kopac@gmail.com', 1), ('63180003', 'Marta', 'Veljak', 1, 'marta.veljak0000@gmail.com', 1);

-- enrolments
INSERT INTO enrolment (id_student, id_study_year, id_study_program, year, type, kind, confirmed) VALUES (1, 1, 1, 1, 'prvi vpis', 'redni', true), (1, 2, 1, 2, 'prvi vpis', 'redni', true), (2, 2, 2, 1, 'prvi vpis', 'redni', true)