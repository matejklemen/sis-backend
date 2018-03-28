# Database structure

**Notes:**
* use `snake_case` for table and attribute names
* **If IntelliJ makes warnings about *Cannot resolve columns...* or *Unable to resolve...* but it compiles and runs successfuly, all you have to do is synchronize your database in IntelliJ! (no need to "Add x to data source...")**

### `student`

Contains student's personal data.

TODO: add home address and temporary address
```sql
id SERIAL PRIMARY KEY,
register_number VARCHAR(8) UNIQUE NOT NULL,
name VARCHAR(30) NOT NULL,
surname VARCHAR(60) NOT NULL,
id_address1 FOREIGN KEY,
id_address2 FOREIGN KEY,
phone_number VARCHAR(255),
email VARCHAR(80) NOT NULL,
id_login FOREIGN KEY,
id_study_program FOREIGN KEY NOT NULL,
id_study_year FOREIGN KEY NUT NULL
```

### `user_login`

Contains data for logins for everyone that can access the system (students and employees)
```sql
id SERIAL PRIMARY KEY,
username VARCHAR(64) UNIQUE NOT NULL,
password VARCHAR(128),
salt INTEGER NOT NULL,
id_user_role FOREIGN KEY
```

### `enrolment`

Contains data about enrolment students have made.

TODO: make seperate table for enrolment types? (example types: *prvi vpis*, *ponovni vpis*, *absolvent*).

TODO: make seperate table for enrolment kinds? (example kinds: *redni*, *izredni*)
```sql
id PRIMARY KEY,
id_student FOREIGN KEY,
id_study_year FOREIGN KEY,
id_study_program FOREIGN KEY,
year INTEGER,
type VARCHAR(20),
kind VARCHAR(20),
confirmed BOOLEAN NOT NULL
```

### `enrolment_token`

Contains data about enrolment tokens availiable to students.

(TODO: describe here the relation between `token` and `enrolment`?)
```sql
id PRIMARY KEY,
id_student FOREIGN KEY,
id_study_year FOREIGN KEY,
id_study_program FOREIGN KEY,
year INTEGER,
type VARCHAR(20),
kind VARCHAR(20),
used BOOLEAN NOT NULL
```

### `study_program`

See csv file for examples (in `entities/src/main/resources/`)

```sql
id VARCHAR(10) PRIMARY KEY,
name VARCHAR,
id_study_degree FOREIGN KEY,
semesters INTEGER
evs_code INTEGER
```

### `study_degree`

See csv file for examples (in `entities/src/main/resources/`)

```sql
id VARCHAR(3) PRIMARY KEY,
name VARCHAR,
```

### `study_year`

Contains data about study years.

Example names: *2017/2018*, *2016/2017*
```sql
id SERIAL PRIMARY KEY,
name VARCHAR(60) NOT NULL
```

### `user_role`

Contains login role definitions.

Example names: *Administrator*, *Student*, *Professor*
```sql
id SERIAL PRIMARY KEY,
name VARCHAR(20) NOT NULL
```

### `address`

Containts people's addresses.

```sql
id SERIAL PRIMARY KEY,
line1 VARCHAR(255) NOT NULL,
line2 VARCHAR(255),
id_post_address FOREIGN KEY,
id_country FOREIGN KEY
```

### `post_address`

Contains post number and addresses. `id == post number`

```sql
id PRIMARY KEY,
name VARCHAR(255) NOT NULL,
```

### `country`

Contains countries. `name` is slovene, `name_iso` is international. `id == country code (int)`

```sql
id INTEGER PRIMARY KEY,
code2 VARCHAR(2) NOT NULL,
code3 VARCHAR(3) NOT NULL,
name VARCHAR,
name_iso VARCHAR
```

# Other notes

If primary key in `entities` is annotated with `@XmlID` and `@XmlElement`, and the same foreign key is annotated with `@XmlIDREF`, only the ID of the object will be passed, not whole object (see `UserLogin.id` and `Student.loginData` for an example).
