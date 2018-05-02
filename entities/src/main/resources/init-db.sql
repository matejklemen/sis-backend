-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

-- user logins
INSERT INTO user_login (id, username, password, id_user_role, salt) VALUES (1, 'joze1', '263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62', 1, 123), (2, 'blazkablatnik@gmail.com', '5c04bcdf01a4553d9b4ebe88b0d6cca9ad763c141f0fef491cbbad71114d24a2d8e633c32179d6c05f4d1adfa29ba3cda2e1cb1bdcd23b2d50abf317e77de41c', 2, 1337), (3, 'prof@fri.uni-lj.si', 'ebbcfecbb97654304f3aee0228236ec4d94fa0593ea1ec8236fc3e2b9e6b01546d9ee62550d8ff30d3df9edc65517caa75dcb6dd358e11a9ec5d7cdf8911b0c0', 3, 321), (4, 'ref@fri.uni-lj.si', 'd858b8524703de2002a21b7798f8ffb239308d0e9cdfd2c6756b7ffbf5d83561c1e5c73239f9b13d568513ea84aea268c7e429b63939aacf8b72be197c5ef725', 4, 666), (5, 'janez@nov.ak', 'db2bb81c724d0e9fc3479bd22355e520849702013d177182a24f9a82ebc3ed84739156e06002c3f4e0af1ac18acfbc0792de666d4a6880029ee6a97a43780f74', 2, 420), (6, 'peter@kopljem.net', 'ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413', 2, 456), (7, 'marta@pod.streho', '7eabd9f1226a6d0c3548c3b37dc749c0aa256c03af4f47f8c214498fad4ded4fa48fc9fc2b7a62d977a56bb2a27bc91787214bcdc944b19f7a0e83bc23eca6f7', 2, 101), (8, 'steff@stef.anio', 'ab15321e6484a0f4dca2dfa4f379ca4bc3cafce543bad6753fe50cdbf65cb93b3049546eba6856980f5d60ec67c8496c38542464357885c4ce4d2be057819f10', 2, 808);
ALTER SEQUENCE user_login_id_seq RESTART WITH 9;
-- address
INSERT INTO address (id, line1, id_post_address, id_country) VALUES (1, 'Generični kraj 9', 1234, 705), (2, 'Ulica zlatih jam 14', 8000, 705), (3, 'Pod orodjem 6b', 8220, 705), (4, 'Pod streho 45', 5243, 705), (5,'Cesta poštenih padcev 11', 1231, 705);
ALTER SEQUENCE address_id_seq RESTART WITH 6;

-- student
INSERT INTO student(id, register_number, name, surname, date_of_birth, place_of_birth, gender, email, phone_number, emso, tax_number, id_address1, id_address2, sending_address, id_citizenship, id_country_of_birth, id_municipality_of_birth, id_login) VALUES (1, '63130001', 'Janez', 'Novak', '1996-01-01', 'Ljubljana', 'M', 'janez@nov.ak', '+386 666 666', '0101996500005', '12345678', 1, NULL, 1, 705, 705, 61, 5),(2, '63180002', 'Peter', 'Kramp', '1994-12-31', 'Novo mesto', 'M', 'peter@kopljem.net', '+386 123 123', '3112994500019', '12345679', 2, 3, 2, 705, 705, 85, 6),(3, '63130002', 'Marta', 'Strešnik', '1996-05-12', NULL, 'Ž', 'marta@pod.streho', NULL, '1205996505019', '12345670', 4, NULL, 1, 643, 643, NULL, 7),(4, '63130003', 'Štefan', 'Čolnar', '1991-02-02', 'Ljubljana', 'M', 'steff@stef.anio', '005318008', '0202991500008', '12345667', 5, 5, 1, 705, 705, 61, 8),(5, '63180001', 'Blažka', 'Blatnik', NULL, NULL, '', 'blazkablatnik@gmail.com', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
ALTER SEQUENCE student_id_seq RESTART WITH 6;

-- enrolments
INSERT INTO enrolment (id, id_student, id_study_year, id_study_program, year, id_study_type, id_study_kind, id_study_form, id_klasius_srv, confirmed) VALUES(1, 1, 1, '1000468', 1, 1, 1, 1, 16204, true),(2, 3, 1, '1000468', 1, 1, 1, 1, 16204, true),(3, 3, 2, '1000468', 2, 1, 1, 1, 16204, true),(4, 3, 3, '1000468', 2, 2, 1, 1, 16204, true),(5, 4, 1, '1000468', 1, 1, 1, 1, 16204, true),(6, 4, 2, '1000468', 2, 1, 1, 1, 16204, true);
ALTER SEQUENCE enrolment_id_seq RESTART WITH 7;

-- student courses requires enrolments to be pre-inserted, so it can't be put into a csv
INSERT INTO student_courses (id_student_courses,id_course,id_enrolment) VALUES ('1','63212','1'),('2','63205','1'),('3','63203','1'),('4','63204','1'),('5','63209','1'),('6','63207','1'),('7','63277','1'),('8','63278','1'),('9','63215','1'),('10','63202','1'),('11','63212','2'),('12','63205','2'),('13','63203','2'),('14','63204','2'),('15','63209','2'),('16','63207','2'),('17','63277','2'),('18','63278','2'),('19','63215','2'),('20','63202','2'),('21','63213','3'),('22','63279','3'),('23','63280','3'),('24','63208','3'),('25','63217','3'),('26','63517','3'),('27','63216','3'),('28','63218','3'),('29','63220','3'),('30','63219','3'),('31','63213','4'),('32','63517','4'),('33','63280','4'),('34','63212','5'),('35','63205','5'),('36','63203','5'),('37','63204','5'),('38','63209','5'),('39','63207','5'),('40','63277','5'),('41','63278','5'),('42','63215','5'),('43','63213','6'),('44','63279','6'),('45','63280','6'),('46','63208','6'),('47','63217','6'),('48','63517','6'),('49','63216','6'),('50','63218','6'),('51','63220','6'),('52','63219','6');

--connect professor with login ID
UPDATE professor SET id_login = 3 WHERE professor.id = 2;

-- enrolment tokens
INSERT INTO enrolment_token (id, freechoice, used, year, id_study_form, id_study_kind, id_klasius_srv, id_student, id_study_program, id_study_year, id_study_type) VALUES (1, false, true, 1, 1, 1, 16204, 1, '1000468', 1, 1),(2, false, true, 2, 1, 1, 16204, 3, '1000468', 3, 2),(3, false, true, 2, 1, 1, 16204, 4, '1000468', 2, 1);
ALTER SEQUENCE enrolment_token_id_seq RESTART WITH 4;

ALTER SEQUENCE course_exam_term_id_course_exam_term_seq RESTART WITH 31;
ALTER SEQUENCE student_courses_id_student_courses_seq RESTART WITH 53;