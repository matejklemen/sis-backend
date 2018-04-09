-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

-- user logins
INSERT INTO user_login (username, password, id_user_role, salt) VALUES ('joze1', '263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62', 1, 123);

-- address
INSERT INTO address (line1, line2, id_post_address, id_country) VALUES ('Kul kraj 15', 'Druga vrstica naslova', 1000, 705), ('Nekje 26', '', 8000, 703);

-- student
INSERT INTO student (register_number, name, surname, id_address1, id_address2, phone_number, email, id_login) VALUES ('63180001', 'Janez', 'Novak', 1, 2, '+386 666 666', 'janez.novak@gmail.com', 1), ('63180018', 'Štefan', 'Puding', 2, 2, '+386 666 666', 'janez.novak@gmail.com', 1);
INSERT INTO student (register_number, name, surname, id_address1, email, id_login) VALUES ('63180002', 'Miha', 'Kopač', 1, 'miha21kopac@gmail.com', 1), ('63180003', 'Marta', 'Veljak', 1, 'marta.veljak0000@gmail.com', 1);

-- enrolments
INSERT INTO enrolment (id_student, id_study_year, id_study_program, year, id_study_type, id_study_kind, confirmed) VALUES (1, 1, 'VT', 1, 1, 1, true), (1, 2, 'VT', 2, 2, 1, true), (2, 2, 'VU', 1, 1, 3, true);