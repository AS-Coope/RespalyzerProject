import csv
import os

# Print the current working directory
print(os.getcwd())

# Open the CSV file and create a reader object
with open('database/diseases.csv', 'r') as csvfile:
    reader = csv.DictReader(csvfile)
    print(reader.fieldnames)

    # Initialize the SQL query
    sql_query = "INSERT INTO public.diseases VALUES "

    # Loop through each row in the CSV file and append values to the SQL query
    for row in reader:
        # Extract the data from the current row
        latitude = row["disease_id"]
        name = row['name']
        location = row['symptoms']
        geotag = row['treatment']
        contact_number = row["description"]
        contact_number2 = row["causes"]

        # Append the values to the SQL query
        sql_query += f"('{latitude}', '{name}', '{location}', '{geotag}', '{contact_number}', '{contact_number2}', "

    # Remove the last comma and space from the SQL query
    sql_query = sql_query[:-2]

    # Add a semicolon to the end of the SQL query
    sql_query += ";"

    # Print the SQL query
    with open(f"diseases.sql", "w") as f:
            f.write(sql_query)
