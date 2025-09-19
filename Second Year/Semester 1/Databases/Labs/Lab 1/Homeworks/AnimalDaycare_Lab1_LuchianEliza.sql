CREATE TABLE Owners
( CNP_Owner INT PRIMARY KEY,
  firstname varchar(70),
  lastname varchar(70),
  phone_number INT,
  email varchar(70),
)

CREATE TABLE Animals
(animal_id INT PRIMARY KEY,
 firstname varchar(70),
 lastname varchar(70),
 species varchar(70),
 breed varchar(70),
 age INT,
 FK_Animals_Owners INT FOREIGN KEY REFERENCES Owners(CNP_Owner)
 )


 CREATE TABLE	Staff
 (CNP_Staff INT PRIMARY KEY,
firstname varchar(70),
 lastname varchar(70),
 phone_number INT,
 email varchar(70),
 years_of_experience INT,
 salary INT,
 
)
CREATE TABLE Veterinarians
(FK_Veterinarians_Staff INT FOREIGN KEY REFERENCES Staff(CNP_Staff) UNIQUE,
expertise varchar(70),
job_title varchar(70),
)
CREATE TABLE Stylist
(FK_Veterinarians_Staff INT FOREIGN KEY REFERENCES Staff(CNP_Staff) UNIQUE,
service_specialization varchar(70),

)


CREATE TABLE Rooms
(room_number INT PRIMARY KEY,
floor_number INT,
)
CREATE TABLE Appointments
(meeting_ID INT PRIMARY KEY,
appointment_time DATETIME,
FK_Appointments_Animals INT FOREIGN KEY REFERENCES Animals(animal_id),
FK_Appointments_Staff INT FOREIGN KEY REFERENCES Staff(CNP_Staff),
FK_Appointments_Rooms INT FOREIGN KEY REFERENCES Rooms(room_number),
)


CREATE TABLE AppointmentBills
(
bill_id INT PRIMARY KEY,
price INT,
paid_status varchar(70),
payment_method varchar(70),
FK_AppointmentBill_Appointments INT FOREIGN KEY REFERENCES Appointments(meeting_ID),
)


CREATE TABLE Expenses
(
expense_name varchar(70) PRIMARY KEY,
amount INT,
type varchar(70),
)
CREATE TABLE Suppliers
( supplier_id INT PRIMARY KEY,
name varchar(70),
location varchar(70),
city varchar(70),
phone_number INT,
email varchar(70)
)
CREATE TABLE Contracts
(
contract_id INT PRIMARY KEY,
paid_stataus varchar(70),
payment_method varchar(70),
FK_Contracts_Expenses varchar(70) FOREIGN KEY REFERENCES Expenses(expense_name),
FK_Contracts_Suppliers INT FOREIGN KEY REFERENCES Suppliers(supplier_id),
)

