Create database LanguageAcademy
go
use LanguageAcademy
go

Create table Languages
(idL INT PRIMARY KEY IDENTITY,
Name varchar(50)
)

CREATE TABLE Classrooms
(idClass INT PRIMARY KEY IDENTITY,
Name varchar(50)
)

CREATE TABLE Teachers
(idT INT PRIMARY KEY IDENTITY,
Name varchar(50), 
Age INT,
check (Age>0),
Gender varchar(50)
)

CREATE TABLE Grupa
(idGroup INT PRIMARY KEY IDENTITY,
idL INT FOREIGN KEY REFERENCES Languages(idL),
Capacity int)

Create table Teaching
(idGroup INT FOREIGN KEY REFERENCES Grupa(idGroup),
idT INT FOREIGN KEY REFERENCES Teachers(idT),
CONSTRAINT pk_Teaching PRIMARY KEY (idT, idGroup)
)

Create table Timetable
(idClass INT FOREIGN KEY REFERENCES Classrooms(idClass),
idGroup INT FOREIGN KEY REFERENCES Grupa(idGroup),
CONSTRAINT pk_Orar PRIMARY KEY (idGroup, idClass),
Date_time datetime default getdate()
)

Create table Students
(idS INT PRIMARY KEY IDENTITY,
Name varchar(50),
Age int,
check (Age>0),
Gender varchar(50)
)

Create table Enrollment
(idS INT FOREIGN KEY REFERENCES Students(idS),
idL INT FOREIGN KEY REFERENCES Languages(idL),
CONSTRAINT pk_Enrollement PRIMARY KEY (idS, idL),
nivel varchar(50)
)

Create table Exam
(idE INT PRIMARY KEY IDENTITY,
Name varchar(50),
ExamDate date NOT NULL,
idL INT FOREIGN KEY REFERENCES LANGUAGES(idL)
)

Create table Result
(idS INT FOREIGN KEY REFERENCES Students(idS),
idE INT FOREIGN KEY REFERENCES Exam(idE),
CONSTRAINT pk_Result PRIMARY KEY (idS, idE),
Grade INT,
CHECK (Grade>=1 and Grade<=10)
)


