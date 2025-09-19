create database ArtworkCompetition
go

use ArtworkCompetition
go

CREATE TABLE School
(SchoolID int PRIMARY KEY IDENTITY,
School_name VARCHAR(20) NOT NULL,
Number int)

CREATE TABLE Teacher
(TeacherID int PRIMARY KEY IDENTITY,
First_Name VARCHAR(20),
Last_Name VARCHAR(20),
Email VARCHAR(30),
PhoneNumber VARCHAR(10),
School int FOREIGN KEY REFERENCES School(SchoolID)
ON DELETE CASCADE)

CREATE TABLE EducationLevel
(LevelID int PRIMARY KEY IDENTITY,
level_name VARCHAR(15))

CREATE TABLE Grade
(GradeID int PRIMARY KEY IDENTITY,
grade int,
education_level int FOREIGN KEY REFERENCES EducationLevel(LevelID))

CREATE TABLE Section 
(SectionID int PRIMARY KEY IDENTITY,
section_name VARCHAR(20))

CREATE TABLE Subsection
(S_ID int FOREIGN KEY REFERENCES Section(SectionID),
ED_ID int FOREIGN KEY REFERENCES EducationLevel(LevelID),
CONSTRAINT pk_subsection PRIMARY KEY(S_ID, ED_ID))

CREATE TABLE Prize
(PrizeID int PRIMARY KEY IDENTITY,
place int,
registration_number VARCHAR(10))

CREATE TABLE Diploma
(DiplomaID int PRIMARY KEY IDENTITY,
registration_number VARCHAR(10))

CREATE TABLE Student
(Id int PRIMARY KEY IDENTITY,
CNP int,
First_Name VARCHAR(20),
Last_Name VARCHAR(20),
Address_ VARCHAR(50),
Grade int FOREIGN KEY REFERENCES Grade(GradeID),
Coordinating_teacher int FOREIGN KEY REFERENCES Teacher(TeacherID),
Observations VARCHAR(100))

CREATE TABLE Artwork
(Title VARCHAR(50),
prize int FOREIGN KEY REFERENCES Prize(PrizeID),
diploma int FOREIGN KEY REFERENCES Diploma(DiplomaID),
section int FOREIGN KEY REFERENCES Section(SectionID),
Creator int FOREIGN KEY REFERENCES Student(Id)
ON DELETE CASCADE,
CONSTRAINT pk_artwork PRIMARY KEY(Creator))