#!/usr/bin/python

import sys

def csv2sql(filename):
  fread = open(filename, "r")

  # first line is table name
  tableName = fread.readline()
  # second line is column names
  columnNames = fread.readline()

  sqlStatement = "INSERT INTO {} ({}) VALUES ".format(tableName.strip(), columnNames.strip().replace(";",","))
  fwrite.write(sqlStatement)

  first = True
  line = fread.readline()
  while line:
    values = ""

    if not first:
      values += ","
    else:
      first = False

    values += "('{}')".format(line.strip().replace("'", "''").replace(";", "','"))

    values = values.replace("'NULL'", "NULL")

    fwrite.write(values)

    line = fread.readline()

  fwrite.write(";\n")
  fread.close()

def directsql(filename):
  fread = open(filename, "r")
  
  line = fread.readline()
  while line:
    fwrite.write(line)
    line = fread.readline()

  fread.close()

# open a file for writing
fwrite = open("generated.sql", "w")
fwrite.write("-- This file is generated with python script and probably SHOULD NOT be edited but generated!\n\n")

arguments = sys.argv
# remove first argument (name of this script)
del arguments[0]

if len(arguments) == 0:
  print("No files given! As arguments, pass files in order you want them to be in generated SQL file.\n" + 
  "You can prepand filename with : (colon) to NOT process file as csv and just append it directly to output.\n" + 
  "Script will generate an generated.sql file.\n\nExample: python csv2sqlinsert some-csv-data.csv anotherone.csv :sqlfiletoalsoappend.sql")
  quit()

# for each argument
for arg in sys.argv:
  filename = arg

  if filename.startswith(":"):
    directsql(filename[1:])
  else:
    csv2sql(filename)

fwrite.close()
