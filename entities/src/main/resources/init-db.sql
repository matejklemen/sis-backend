-- nevem zakaj mora bit v eni vrstici ceu ukaz, probu sm sam values nastet po vrsticah pa ni delal

-- user logins
INSERT INTO user_login (id,password,salt,username,id_user_role) VALUES ('1','263fec58861449aacc1c328a4aff64aff4c62df4a2d50b3f207fa89b6e242c9aa778e7a8baeffef85b6ca6d2e7dc16ff0a760d59c13c238f6bcdc32f8ce9cc62','123','joze1','1'),('2','5c04bcdf01a4553d9b4ebe88b0d6cca9ad763c141f0fef491cbbad71114d24a2d8e633c32179d6c05f4d1adfa29ba3cda2e1cb1bdcd23b2d50abf317e77de41c','1337','blazkablatnik@gmail.com','2'),('3','ebbcfecbb97654304f3aee0228236ec4d94fa0593ea1ec8236fc3e2b9e6b01546d9ee62550d8ff30d3df9edc65517caa75dcb6dd358e11a9ec5d7cdf8911b0c0','321','prof@fri.uni-lj.si','3'),('4','d858b8524703de2002a21b7798f8ffb239308d0e9cdfd2c6756b7ffbf5d83561c1e5c73239f9b13d568513ea84aea268c7e429b63939aacf8b72be197c5ef725','666','ref@fri.uni-lj.si','4'),('5','db2bb81c724d0e9fc3479bd22355e520849702013d177182a24f9a82ebc3ed84739156e06002c3f4e0af1ac18acfbc0792de666d4a6880029ee6a97a43780f74','420','janez@nov.ak','2'),('6','ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413','456','peter@kopljem.net','2'),('7','7eabd9f1226a6d0c3548c3b37dc749c0aa256c03af4f47f8c214498fad4ded4fa48fc9fc2b7a62d977a56bb2a27bc91787214bcdc944b19f7a0e83bc23eca6f7','101','marta@pod.streho','2'),('8','ab15321e6484a0f4dca2dfa4f379ca4bc3cafce543bad6753fe50cdbf65cb93b3049546eba6856980f5d60ec67c8496c38542464357885c4ce4d2be057819f10','808','steff@stef.anio','2'),('9','99a5fe9d40e246de0bb911818f79cc03718148ca33c5e92f2fbbdc7907b54b0520e60de994fe4331f905eced10986c572315f47097f16238d5de491043bacabc','163734400','sz8003@student.uni-lj.si','2'),('10','a635bae3313801e933c8a6e4df03484deecbb11a5a91e866186042569a4349cd8458807c3048ca9cbcc26200c0647e52ed64842dc3ee830d499ddb052ebaf869','-121163117','jd8004@student.uni-lj.si','2'),('11','3fd39af9b15a8c647423e74287c52719d3b4e5f1463c3a03de4084a05670d32b93fdc6732bc41d6dd4c3918fc2ac21fffc8a4e8aadcc2e1411f53a328c64e006','-674793274','mj8005@student.uni-lj.si','2'),('12','f6535d6f56c1b37f91d0e7c3b4250e245a3cfef60bd4c354e73b5db7d56dc8e3362c72d9f36143bd48b28039bab540abd4f39484a79e51f983ba93369061bae9','-1648194537','lh8006@student.uni-lj.si','2'),('13','994fa9c537d8068f22c639c1f4e25ba90e227f922a06f8ef1a38e146ee754332457104941c299e37f5cdb45473a0cf1e5dfb04f8298a0eb2df4112f26a2aa450','-1190932726','žr8007@student.uni-lj.si','2'),('14','9a557bffde1523d7755acd93fbfe9ec7710fea07dfa6174a85ef9e7f8431a36fbaf9711fab947280259f1de79b8294849dedaf98f668771c7f3126937f3c6841','1269228366','tz8008@student.uni-lj.si','2'),('15','b0716094e48aa1f90c46522165ba15f499e20a2c8bc11732e811cab484d1576b32769c212801f68c3b39fc7297de42c20efc3af76d6e459bb68b16b3290ae1e3','-1052744078','rb8009@student.uni-lj.si','2'),('16','21cbf967a155b76cfdb39d23b64e27ff71eff7f93949ef370145264f312da0712902e874862bfd77370bdb27011cb71805e0a673c0a989b36ce8bdb02a732124','-926210828','nk8010@student.uni-lj.si','2'),('17','893343307543f0e85121c3c829c65933b3a3609cb3ce5905648b340c78336b79eeacae265003cf132f59d6fce3098754ef6db0d14eebac25fed681ca11ba74e3','-1620454342','cl8011@student.uni-lj.si','2'),('18','9fa490fa5b35733d201ed3ac531520bfb40798594f21492a64280cf1bd5011c0c7cee66742c31298b8b37b6373d84860219ee47eba854b62d822f917d9241aee','-774544758','ip8012@student.uni-lj.si','2'),('19','ce61c160ca6ff7365a1b5cac899fc010bae3028ad3a62ddbb9a56be2f43eb122fa68ca1843bd6ed52911a3372b686bd61359792993c5eb72c6c104c8b4fd47c0','1459725057','jj8013@student.uni-lj.si','2');
ALTER SEQUENCE user_login_id_seq RESTART WITH 20;
-- address
INSERT INTO address (id, line1, id_post_address, id_country) VALUES (1, 'Generični kraj 9', 1234, 705), (2, 'Ulica zlatih jam 14', 8000, 705), (3, 'Pod orodjem 6b', 8220, 705), (4, 'Pod streho 45', 5243, 705), (5,'Cesta poštenih padcev 11', 1231, 705);
ALTER SEQUENCE address_id_seq RESTART WITH 6;

-- student
INSERT INTO student (id,date_of_birth,email,emso,gender,name,phone_number,place_of_birth,register_number,sending_address,surname,tax_number,id_address1,id_address2,id_citizenship,id_country_of_birth,id_municipality_of_birth,id_study_program,id_login) VALUES ('1','1996-01-01','janez@nov.ak','0101996500005','M','Janez','+386 666 666','Ljubljana','63130001','1','Novak','12345678','1',NULL,'705','705','61',NULL,'5'),('2','1994-12-31','peter@kopljem.net','3112994500019','M','Peter','+386 123 123','Novo mesto','63180002','2','Kramp','12345679','2','3','705','705','85',NULL,'6'),('3','1996-05-12','marta@pod.streho','1205996505019','Ž','Marta',NULL,NULL,'63130002','1','Strešnik','12345670','4',NULL,'643','643',NULL,NULL,'7'),('4','1991-02-02','steff@stef.anio','0202991500008','M','Štefan','005318008','Ljubljana','63130003','1','Čolnar','12345667','5','5','705','705','61',NULL,'8'),('5',NULL,'blazkablatnik@gmail.com',NULL,'','Blažka',NULL,NULL,'63180001',NULL,'Blatnik',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('6',NULL,'szore.fake@gmail.com',NULL,'-','Simon',NULL,NULL,'63180003','1','Zore',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','9'),('7',NULL,'jdoe.fake@gmail.com',NULL,'-','John',NULL,NULL,'63180004','1','Doe',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','10'),('8',NULL,'mjelenc.fake@gmail.com',NULL,'-','Marko',NULL,NULL,'63180005','1','Jelenc',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','11'),('9',NULL,'lhribar.fake@gmail.com',NULL,'-','Luka',NULL,NULL,'63180006','1','Hribar',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','12'),('10',NULL,'zrink.fake@gmail.com',NULL,'-','Žan',NULL,NULL,'63180007','1','Rink',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','13'),('11',NULL,'tzupanc.fake@gmail.com',NULL,'-','Teja',NULL,NULL,'63180008','1','Zupanc',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','14'),('12',NULL,'rberdnik.fake@gmail.com',NULL,'-','Rok',NULL,NULL,'63180009','1','Berdnik',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','15'),('13',NULL,'nkos.fake@gmail.com',NULL,'-','Nik',NULL,NULL,'63180010','1','Kos',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','16'),('14',NULL,'clukavcic.fake@gmail.com',NULL,'-','Cene',NULL,NULL,'63180011','1','Lukavčič',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','17'),('15',NULL,'ipri.fake@gmail.com',NULL,'-','Imenko',NULL,NULL,'63180012','1','Priimkovič',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','18'),('16',NULL,'jjakomin.fake@gmail.com',NULL,'-','Jaka',NULL,NULL,'63180013','1','Jakomin',NULL,NULL,NULL,NULL,NULL,NULL,'1000468','19');
ALTER SEQUENCE student_id_seq RESTART WITH 17;

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

ALTER SEQUENCE course_exam_term_id_course_exam_term_seq RESTART WITH 83;
ALTER SEQUENCE student_courses_id_student_courses_seq RESTART WITH 53;