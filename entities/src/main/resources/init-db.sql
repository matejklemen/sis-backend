-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

-- user logins
INSERT INTO user_login (id, username, password, id_user_role, salt) VALUES (1, 'joze1', '263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62', 1, 123), (2, 'blazkablatnik@gmail.com', '5c04bcdf01a4553d9b4ebe88b0d6cca9ad763c141f0fef491cbbad71114d24a2d8e633c32179d6c05f4d1adfa29ba3cda2e1cb1bdcd23b2d50abf317e77de41c', 2, 1337), (3, 'fejk.mail@fri.uni-lj.si', 'ebbcfecbb97654304f3aee0228236ec4d94fa0593ea1ec8236fc3e2b9e6b01546d9ee62550d8ff30d3df9edc65517caa75dcb6dd358e11a9ec5d7cdf8911b0c0', 3, 321), (4, 'ref@fri.uni-lj.si', 'd858b8524703de2002a21b7798f8ffb239308d0e9cdfd2c6756b7ffbf5d83561c1e5c73239f9b13d568513ea84aea268c7e429b63939aacf8b72be197c5ef725', 4, 666), (5, 'janez@nov.ak', 'db2bb81c724d0e9fc3479bd22355e520849702013d177182a24f9a82ebc3ed84739156e06002c3f4e0af1ac18acfbc0792de666d4a6880029ee6a97a43780f74', 2, 420), (6, 'peter@kopljem.net', 'ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413', 2, 456), (7, 'marta@pod.streho', '7eabd9f1226a6d0c3548c3b37dc749c0aa256c03af4f47f8c214498fad4ded4fa48fc9fc2b7a62d977a56bb2a27bc91787214bcdc944b19f7a0e83bc23eca6f7', 2, 101), (8, 'steff@ff.fff', 'ab15321e6484a0f4dca2dfa4f379ca4bc3cafce543bad6753fe50cdbf65cb93b3049546eba6856980f5d60ec67c8496c38542464357885c4ce4d2be057819f10', 2, 808);
ALTER SEQUENCE user_login_id_seq RESTART WITH 9;
-- address
INSERT INTO address (line1, id_post_address, id_country) VALUES ('Generični kraj 9', 1234, 705), ('Ulica zlatih jam 14', 8000, 705), ('Pod orodjem 6b', 8220, 705), ('Pod streho 45', 5243, 705), ('Cesta poštenih padcev 11', 1231, 705);

-- student
INSERT INTO student(register_number, name, surname, date_of_birth, place_of_birth, gender, email, phone_number, emso, tax_number, id_address1, id_address2, sending_address, id_citizenship, id_country_of_birth, id_municipality_of_birth, id_login) VALUES ('63131801', 'Janez', 'Novak', '1996-01-01', 'Ljubljana', 'M', 'janez@nov.ak', '+386 666 666', '0101996500005', '12345678', 1, NULL, 1, 705, 705, 61, 5), ('63161802', 'Peter', 'Kramp', '1994-12-31', 'Novo mesto', 'M', 'peter@kopljem.net', '+386 123 123', '3112994500019', '12345679', 2, 3, 2, 705, 705, 85, 6), ('63131803', 'Marta', 'Strešnik', '1996-05-12', NULL, 'Ž', 'marta@pod.streho', NULL, '1205996505019', '12345670', 4, NULL, 1, 643, 643, NULL, 7), ('63131804', 'Štefan', 'Pirh', '1991-02-02', 'Ljubljana', 'M', 'steff@ff.fff', '005318008', '0202991500008', '12345667', 5, 5, 1, 705, 705, 61, 8), ('63156805', 'Blažka', 'Blatnik', '1996-02-02', 'Ljubljana', 'M', 'blazkablatnik@gmail.com', '005318008', '0202996504001', '12345667', 5, 5, 1, 705, 705, 61, 2);

-- enrolments
INSERT INTO enrolment (id_student, id_study_year, id_study_program, year, id_study_type, id_study_kind, id_study_form, id_klasius_srv, confirmed) VALUES (1, 1, '1000468', 1, 1, 1, 1, 16204, true), (3, 1, '1000468', 1, 1, 1, 1, 16204, true), (3, 2, '1000468', 2, 1, 1, 1, 16204, true), (3, 3, '1000468', 2, 2, 1, 1, 16204, true), (4, 1, '1000468', 1, 1, 1, 1, 16204, true), (4, 2, '1000468', 2, 1, 1, 1, 16204, true);

--connect professor with login ID
UPDATE professor SET id_login = 3 WHERE professor.id = 17;