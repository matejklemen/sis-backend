# csv2sqlinsert.py

Script is saved in `/entities/src/main/resources/`.

## What it does

`csv2sqlinsert` will open all .csv files in current directory and wrote SQL INSERTs into a new `generated.sql` file.
After it's done with csv files, it will open all .sql files (except generated.sql) and append those sql files to generated.sql

## CSV file style
- values are seperated with `;` (semicolon)
- first line is **table name**
- second line is **column names**
- all remaining files are row values, obviously in the same order as column names in second line
- notes: 
    - there should be **no empty lines** at the end of file or anywhere else (script doesn't take care of that)!
    - leaving empty value in csv is treated as an empty string! If you want to insert null, you have to explicity write `NULL` in csv file
