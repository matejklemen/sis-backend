
-- staff (ref and joze1)
INSERT INTO staff (id, first_name, last_name1, last_name2, id_login) VALUES (1, 'Referen', 'Tka', 'Fri', '4'), (2, 'Jože', 'Može', NULL, '1');
--connect professor with login ID
UPDATE professor SET id_login = 3 WHERE professor.id = 2;

-- restart id sequences where ids were inserted manually
ALTER SEQUENCE address_id_seq RESTART WITH 10;
ALTER SEQUENCE course_exam_term_id_course_exam_term_seq RESTART WITH 300;
ALTER SEQUENCE student_courses_id_student_courses_seq RESTART WITH 3000;
ALTER SEQUENCE course_organization_id_course_organization_seq RESTART WITH 400;
ALTER SEQUENCE exam_sign_up_id_seq RESTART WITH 4000;
ALTER SEQUENCE enrolment_id_seq RESTART WITH 400;
ALTER SEQUENCE enrolment_token_id_seq RESTART WITH 400;
ALTER SEQUENCE professor_id_seq RESTART WITH 100;
ALTER SEQUENCE student_id_seq RESTART WITH 200;
ALTER SEQUENCE user_login_id_seq RESTART WITH 200;
ALTER SEQUENCE staff_id_seq RESTART WITH 3;
ALTER SEQUENCE curriculum_id_curriculum_seq RESTART WITH 3000;
