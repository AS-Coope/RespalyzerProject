import csv
import os

# Print the current working directory
print(os.getcwd())

# Open the CSV file and create a reader object
with open('database/medical_centres.csv', 'r') as csvfile:
    reader = csv.DictReader(csvfile)
    print(reader.fieldnames)

    # Initialize the SQL query
    sql_query = "INSERT INTO Medical Centres (name, location, geotag, contact_number, contact_number2) VALUES "

    # Loop through each row in the CSV file and append values to the SQL query
    for row in reader:
        # Extract the data from the current row
        name = row['ï»¿name']
        location = row['location']
        geotag = row['geotag']
        contact_number = row["contact_number"]
        contact_number2 = row["contact_number2"]

        # Append the values to the SQL query
        sql_query += f"('{name}', '{location}', '{geotag}', '{contact_number}', '{contact_number2}'), "

    # Remove the last comma and space from the SQL query
    sql_query = sql_query[:-2]

    # Add a semicolon to the end of the SQL query
    sql_query += ";"

    # Print the SQL query
    with open(f"medical_centres.sql", "w") as f:
            f.write(sql_query)
