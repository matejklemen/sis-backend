# sis
A student information system made for a software engineering course.

## First time set-up:
* Set up application: Run -> Edit Configurations -> New "Application" -> main class: "**com.kumuluz.ee.EeApplication**"
* Set up local DB: 
	1.  run Docker -> "**docker run -d --name postgres-jdbc -e POSTGRES\_PASSWORD=postgres -e POSTGRES\_DB=sis -p 5432:5432 postgres:latest**"
	2.  "**docker start postgres-jdbc**"
* Add source in IntelliJ:
	1.  Database -> Add -> Postgres
	2.  Host: localhost or ip
	3.  Database: sis
	4.  User: postgres
	5.  Password: postgres
	6.  Test Connection

## Development cycle:
1.  docker start postgres-jdbc  
2.  maven package 