## Create all tables in postgres:

`NOTE: use snake case`

1. student_data (All personal data of students -> init at data import)
CREATE TABLE public.student_data
```sql
(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  surname VARCHAR(30) NOT NULL,
  course VARCHAR(7) NOT NULL,
  email VARCHAR(60) NOT NULL,
  student_id INT NOT NULL,
  login_id INT NOT NULL
);
```