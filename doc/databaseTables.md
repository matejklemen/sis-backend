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

### `token`

Contains data about enrolment tokens availiable to students.

(TODO: describe here the relation between `token` and `enrolment`?)
```sql
id PRIMARY KEY,
id_student FOREIGN KEY,
id_study_year FOREIGN KEY,
id_study_program FOREIGN KEY,
year INTEGER,
used BOOLEAN NOT NULL
```

### `study_program`

Contains data about study programs.

Example names: *Računalništvo in informatika (UNI)*, *Računalništvo in informatika (VSŠ)*, *Računalništvo in matematika*
```sql
id SERIAL PRIMARY KEY,
name VARCHAR(120) NOT NULL
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

# Other notes

If primary key in `entities` is annotated with `@XmlID` and `@XmlElement`, and the same foreign key is annotated with `@XmlIDREF`, only the ID of the object will be passed, not whole object (see `UserLogin.id` and `Student.loginData` for an example).
