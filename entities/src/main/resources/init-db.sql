-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

--connect professors with login ID
UPDATE professor SET id_login = 3 WHERE professor.id = 2; -- Igor Kononenko
UPDATE professor SET id_login = 103 WHERE professor.id = 5; -- Blaz Zupan
UPDATE professor SET id_login = 98 WHERE professor.id = 30; -- Bo"stjan Slivnik
UPDATE professor SET id_login = 104 WHERE professor.id = 40; -- Gasper Fijavz
UPDATE professor SET id_login = 99 WHERE professor.id = 23; -- Nezka Mramor Kosta 
UPDATE professor SET id_login = 100 WHERE professor.id = 24; -- Miha Mraz
UPDATE professor SET id_login = 101 WHERE professor.id = 33; -- Nikolaj Zimic
UPDATE professor SET id_login = 102 WHERE professor.id = 1; -- Aleksandar Jurisic

ALTER SEQUENCE address_id_seq RESTART WITH 8;
ALTER SEQUENCE agreement_id_agreement_seq RESTART WITH 7;
ALTER SEQUENCE course_exam_term_id_course_exam_term_seq RESTART WITH 300;
ALTER SEQUENCE student_courses_id_student_courses_seq RESTART WITH 3250;
ALTER SEQUENCE course_organization_id_course_organization_seq RESTART WITH 400;
ALTER SEQUENCE exam_sign_up_id_seq RESTART WITH 4200;
ALTER SEQUENCE enrolment_id_seq RESTART WITH 450;
ALTER SEQUENCE enrolment_token_id_seq RESTART WITH 450;
ALTER SEQUENCE professor_id_seq RESTART WITH 100;
ALTER SEQUENCE student_id_seq RESTART WITH 200;
ALTER SEQUENCE user_login_id_seq RESTART WITH 200;
ALTER SEQUENCE staff_id_seq RESTART WITH 3;
ALTER SEQUENCE curriculum_id_curriculum_seq RESTART WITH 3000;