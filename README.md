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
* **(Ta korak ni nujen? Tabele se naredijo same)** [Create all tables id db](doc/databaseTables.md) and assign data source (Persistence Tab)

## Development cycle:
1.  `docker start postgres-jdbc`
2.  `maven clean package`

## API documentation
The project uses OpenAPI specification 3. Documentation hosted on:
http://localhost:8080/api-specs/ui (when running locally)
