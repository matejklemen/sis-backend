-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

-- user logins
INSERT INTO user_login (username, password, id_user_role, salt) VALUES ('joze1', '263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62', 1, 123), ('mk3141@student.uni-lj.si', '5c04bcdf01a4553d9b4ebe88b0d6cca9ad763c141f0fef491cbbad71114d24a2d8e633c32179d6c05f4d1adfa29ba3cda2e1cb1bdcd23b2d50abf317e77de41c', 2, 1337), ('fejk.mail@fri.uni-lj.si', 'ebbcfecbb97654304f3aee0228236ec4d94fa0593ea1ec8236fc3e2b9e6b01546d9ee62550d8ff30d3df9edc65517caa75dcb6dd358e11a9ec5d7cdf8911b0c0', 3, 321), ('ref@fri.uni-lj.si', '3cb32dbc41cda352b023833eb6ea9d23e0be5f821cfe5cec1d426936e5d35167d3c38b9e14cee2f688500b7361ab2dd4849b0c45ffaef9c88daa3484b703abb0', 4, 666);

-- address
INSERT INTO address (line1, id_post_address, id_country) VALUES ('Generični kraj 9', 1234, 705), ('Ulica zlatih jam 14', 8000, 705), ('Pod orodjem 6b', 8220, 705), ('Pod streho 45', 5243, 705), ('Cesta poštenih padcev 11', 1231, 705);

-- student
INSERT INTO student(register_number, name, surname, date_of_birth, place_of_birth, gender, email, phone_number, emso, tax_number, id_address1, id_address2, sending_address, id_citizenship, id_country_of_birth, id_municipality_of_birth, id_login) VALUES ('63181801', 'Janez', 'Novak', '1996-01-01', 'Ljubljana', 'M', 'janez@nov.ak', '+386 666 666', '1234567891234', '12345678', 1, NULL, 1, 705, 705, 61, NULL), ('63181802', 'Peter', 'Kramp', '1994-12-31', 'Novo mesto', 'M', 'peter@kopljem.net', '+386 123 123', '1234567891235', '12345679', 2, 3, 2, 705, 705, 85, NULL), ('63181803', 'Marta', 'Strešnik', '1996-05-12', NULL, 'Ž', 'marta@pod.streho', NULL, '1234567891236', '12345670', 4, NULL, 1, 643, 643, NULL, NULL), ('63181804', 'Štefan', 'Pirh', '1991-02-02', 'Ljubljana', 'M', 'steff@ff.fff', '005318008', '1234567891237', '12345667', 5, 5, 1, 705, 705, 61, NULL);

-- enrolments
INSERT INTO enrolment (id_student, id_study_year, id_study_program, year, id_study_type, id_study_kind, id_study_form, confirmed) VALUES (1, 1, 'VT', 1, 1, 1, 1, true), (1, 2, 'VT', 2, 2, 1, 1, true), (2, 2, 'VU', 1, 1, 3, 2, true);