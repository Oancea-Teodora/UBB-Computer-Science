# Importăm biblioteca pandas pentru a manipula datele / Import the pandas library for data manipulation
import pandas as pd


# Inițializăm o listă goală pentru a stoca rezultatele / Initialize an empty list to store the results
result = []

#############
# Subtask 1 #
#############

# Preprocesăm fișierul de antrenare și salvăm versiunea preprocesată în fișierul clean_train_data.csv
# We preprocess the training file and save the preprocessed version in the clean_train_data.csv file
with open("train_data.csv", "r") as train_data_file:
    with open("clean_train_data.csv", "w") as clean_train_data_file:
        t=2
        ###############################################################################################
        # REZOLVAȚI PRIMUL SUBTASK / SOLVE THE FIRST SUBTASK
        #
        # Asigură-te că salvezi liniile care nu au fost corupte în clean_train_data.csv
        # Make sure you save the lines that have not been corrupted to clean_train_data.csv
        #
        ###############################################################################################

# Citim fișierul CSV cu datele de antrenare (versiunea preprocesată) și afișăm primele 5 rânduri
# Read the CSV file which contains the training data (preprocessed version) and print the first 5 rows
clean_df_train = pd.read_csv("clean_train_data.csv")
print(clean_df_train.head())

# Iterăm prin fiecare rând al setului de antrenare preprocesat / Iterate through the rows of the preprocessed training set
for _, row in clean_df_train.iterrows():
    result.append(
        {
            "subtaskID": 1,  # ID-ul subtask-ului / The ID of the subtask
            "datapointID": row["datapointID"],  # ID-ul datapoint-ului din rândul curent / The ID of the current row's datapoint
            "answer": row["Class"].astype(int),  # Răspunsul pentru subtask-ul curent / The answer for the current subtask
        }
    )


#############
# Subtask 2 #
#############

# Citim fișierul CSV cu datele de test și afișăm primele 5 rânduri
# Read the test CSV file and print the first 5 rows
df_test = pd.read_csv("test_data.csv")
print(df_test.head())

# Adăugăm o coloană nouă în setul de date de test. Aici vom pune predicțiile modelului.
# Inițial setăm valoarea 0 pentru toate rândurile.. Facem acest lucru pentru a putea încărca
# în platformă soluția primei cerințe și fără să rezolvăm a doua cerință.
#
# Add a new column to the test dataset. The model’s predictions will be stored here.
# We initially set all values to 0. We do this so that we can upload
# the solution of the first subtask to the platform even without solving the second subtask.
df_test["Class"] = 0

###############################################################################################
# REZOLVAȚI AL DOILEA SUBTASK / SOLVE THE SECOND SUBTASK
#
# Asigură-te că stochezi corect predicțiile în df_test["Class"]
# Make sure you correctly store the predictions in df_test["Class"]
#
###############################################################################################

# Iterăm prin fiecare rând al setului de test / Iterate through the rows of the test set
for _, row in df_test.iterrows():
    result.append(
        {
            "subtaskID": 2,  # ID-ul subtask-ului / The ID of the subtask
            "datapointID": row["datapointID"],  # ID-ul datapoint-ului din rândul curent / The ID of the current row's datapoint
            "answer": row["Class"].astype(int),  # Răspunsul pentru subtask-ul curent / The answer for the current subtask
        }
    )

# Creăm un DataFrame cu rezultatele obținute / Create a dataframe with the obtained results
df_output = pd.DataFrame(result)

# Afișăm primele 5 rânduri din DataFrame-ul rezultat / Print the first 5 rows
print(df_output.head())

# Salvăm rezultatele într-un fișier CSV pe care să-l putem încărca în platformă
# Save the results in a CSV file which can be uploaded to the platform
df_output.to_csv("output.csv", index=False)
