# sis

A student information system made for a software engineering course.

## First time set-up:

* Set up application: Run -> Edit Configurations -> New "Application" -> set main class to: `com.kumuluz.ee.EeApplication` and "Use classpath of module:" to `api` 
* Set up local DB with Docker: 
	1.  `docker run -d --name postgres-jdbc -e POSTGRES\_PASSWORD=postgres -e POSTGRES\_DB=sis -p 5432:5432 postgres:latest`
	2.  `docker start postgres-jdbc` (if not already running)
* Add source in IntelliJ:
	1.  Database -> Add -> Postgres
	2.  Host: localhost or ip
	3.  Database: sis
	4.  User: postgres
	5.  Password: postgres
	6.  Test Connection
* In `entities/src/main/resources/config.yaml`, update jdbc/sisTestDB `connection-url` parameter's IP to match your docker IP.
* Optional: assign data source (Persistence Tab)

## Development cycle:
1.  `docker start postgres-jdbc`
2.  `maven clean package`

## API documentation
The project uses OpenAPI specification 3. Documentation hosted on:
http://localhost:8080/api-specs/ui (when running locally)

## Using API filters

Examples on how to use filters, offsets and limits are availiable at [kumuluzee-rest#examples](https://github.com/kumuluz/kumuluzee-rest#examples).

## Sample users
|     Role     | Role id |         Username        | Password | Enrolments | Study program |
|:------------:|:-------:|:-----------------------:|:--------:|:-------------:|:-------------:|
|  Skrbnik/ca  |    1    |          joze1          |    123   |       /       |       /       |
|  Študent/ka  |    2    |       blazkablatnik@gmail.com      |    123   |       none (no data)      |      whatever      |
|  Študent/ka  |    2    |    peter@kopljem.net    |    123   |       none (but with data)      |      whatever      |
|  Študent/ka  |    2    |       janez@nov.ak      |    123   |      1     |      BUN      |
|  Študent/ka  |    2    |     marta@pod.streho    |    123   |       1,2,2       |      BUN      |
|  Študent/ka  |    2    |       steff@stef.anio      |    123   |       1,2       |      BUN      |
| Profesor/ica |    3    | prof@fri.uni-lj.si |    123   |       /       |       /       |
|  Referent/ka |    4    |    ref@fri.uni-lj.si    |    123   |       /       |       /       |
|  Študent/ka  |    2    | sz8003@student.uni-lj.si | sz_63180003 | 1, 2, 3 |       BUN     |
|  Študent/ka  |    2    | jd8004@student.uni-lj.si | jd_63180004 | 1, 1, 1 (izredni) |     BUN     |
|  Študent/ka  |    2    | mj8005@student.uni-lj.si | mj_63180005 | 1, 2 | BUN |
|  Študent/ka  |    2    | lh8006@student.uni-lj.si | lh_63180006 | 1, 2 | BUN |
|  Študent/ka  |    2    | žr8007@student.uni-lj.si | žr_63180007 | 1, 2, 2 | BUN |
|  Študent/ka  |    2    | tz8008@student.uni-lj.si | tz_63180008 | 1, 2, 3, 3 | BUN |
|  Študent/ka  |    2    | rb8009@student.uni-lj.si | rb_63180009 | 1 | BUN |
|  Študent/ka  |    2    | nk8010@student.uni-lj.si | nk_63180010 | 1, 1, 2 | BUN |
|  Študent/ka  |    2    | cl8011@student.uni-lj.si | cl_63180011 | 1, 1 | BUN |
|  Študent/ka  |    2    | ip8012@student.uni-lj.si | ip_63180012 | 1, 2, 2, 2 (izredni) | BUN |
|  Študent/ka  |    2    | jj8013@student.uni-lj.si | jj_63180013 | 1 | BUN |
|  