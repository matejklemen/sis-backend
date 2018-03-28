#!/usr/bin/python

import sys
import os

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

# open each csv file and process it
for file in sorted(os.listdir(".")):
  if file.endswith(".csv"):
    print("Processing csv file " + file + "...")
    csv2sql(file)
    print("Processing csv file " + file + " done!")

# open each sql file and append it
for file in sorted(os.listdir(".")):
  if file == "generated.sql":
    continue

  if file.endswith(".sql"):
    print("Appending sql file " + file + "...")
    directsql(file)
    print("Appending sql file " + file + " done!")

fwrite.close()
print("--- SQL code has been written into generated.sql ---\nBye!\n")