use Book_Club3


CREATE TABLE Members (
	members_id int PRIMARY KEY,
	first_name varchar(70),
	second_name varchar(70),
	email varchar(70),
	join_year int,
	member_password varchar(70)
)

CREATE TABLE Books (
	book_id int PRIMARY KEY,
	title varchar(70),
	author varchar(70),
	genre varchar(70),
	published_year varchar (70),
)

CREATE TABLE Meetings (
	meeting_id int PRIMARY KEY,
	meeting_date DATE,
	location_place varchar(70),
	fk_Meetings_Books int FOREIGN KEY references Books(book_id),
)

CREATE TABLE Reading_Sessions (
	reading_id int PRIMARY KEY,
	session_date DATE,
	topic varchar(70),
	fk_ReadingSessions_Books int FOREIGN KEY references Books(book_id),
)

CREATE TABLE Book_Requests (
	request_id int PRIMARY KEY,
	book_title varchar(70),
	book_author varchar(70),
	request_status varchar(70),
	--fk_BookRequests_Members INT,
    --CONSTRAINT fk_book_request_member FOREIGN KEY (fk_BookRequests_Members) REFERENCES Members(members_id)
	fk_BookRequests_Members int FOREIGN KEY references Members(members_id),
)


CREATE TABLE Member_Attendance (
	fk_MemberAttendance_Members int FOREIGN KEY references Members(members_id),
	fk_MemberAttendance_Meetings int FOREIGN KEY references Meetings(meeting_id),
	PRIMARY KEY (fk_MemberAttendance_Members,fk_MemberAttendance_Meetings),
)

CREATE TABLE Disscusion_Topics (
	topic_id int PRIMARY KEY,
	discussion_title varchar(70),
	discussion_date DATE,
	fk_DisscusionTopics_Meetings int FOREIGN KEY references Meetings(meeting_id),
)

CREATE TABLE Roles (
	role_id int PRIMARY KEY,
	role_name varchar(70),
	fk_Roles_Members int FOREIGN KEY references Members(members_id),
)

CREATE TABLE Book_Recomandations (
	fk_BookRecomandations_Books int FOREIGN KEY references Books(book_id),
	fk_BookRecomandations_Members int FOREIGN KEY references Members(members_id),
	PRIMARY KEY (fk_BookRecomandations_Books, fk_BookRecomandations_Members),
	recomandation_date DATE,
)

CREATE TABLE Book_Reviews (
	review_id int PRIMARY KEY,
	rating int,
	review_text varchar(70),
	fk_BookReviews_Books int FOREIGN KEY references Books(book_id),
)

--1. FROM
--2. WHERE
--3. GROUP BY
--4. HAVING
--5. SELECT
--6. DISTINCT
--7. ORDER BY
--8. TOP


-- INSERT DATA

INSERT INTO Members values (1,'Cristian', 'Popescu', 'cristianpopescu@yahoo.com', 2018, 'cristian123'),(2, 'Ana', 'Ionescu', 'anaionescu@gmail.com', 2019, 'ana123'),
(3, 'Andrei', 'Georgescu', 'andrei.georgescu@hotmail.com', 2020, 'andrei123'),
(4, 'Maria', 'Radu', 'mariaradu@yahoo.com', 2018, 'maria123'),
(5, 'Diana', 'Mihailescu', 'dianamihailescu@gmail.com', 2021, 'diana123'),
(6, 'Alexandru', 'Marin', 'alexandrumarin@yahoo.com', 2022, 'alexandru123'),
(7, 'Ioana', 'Constantin', 'ioanaconstantin@hotmail.com', 2020, 'ioana123'),
(8, 'Paul', 'Popa', 'paulpopa@gmail.com', 2019, 'paul123'),
(9, 'Elena', 'Bucur', 'elenabucur@yahoo.com', 2023, 'elena123'),
(10, 'Stefan', 'Dumitrescu', 'stefandumitrescu@gmail.com', 2021, 'stefan123');

INSERT INTO Books values (1,'Hotul de carti','Markus Zusak','Fictiune istorica', 2005), (2, '1984', 'George Orwell', 'Dystopian', 1949),
(3, 'Moby Dick', 'Herman Melville', 'Adventure', 1851),
(4, 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 1960),
(5, 'The Great Gatsby', 'F. Scott Fitzgerald', 'Classic', 1925),
(6, 'Pride and Prejudice', 'Jane Austen', 'Romance', 1813),
(7, 'The Catcher in the Rye', 'J.D. Salinger', 'Fiction', 1951),
(8, 'Brave New World', 'Aldous Huxley', 'Dystopian', 1932),
(9, 'War and Peace', 'Leo Tolstoy', 'Historical Fiction', 1869),
(10, 'The Hobbit', 'J.R.R. Tolkien', 'Fantasy', 1937),
(11, 'The Hobbit 2', 'J.R.R. Tolkien', 'Fantasy', 1938);


INSERT INTO Meetings values (1,'2023-11-29','Library',2),(2, '2023-12-05', 'Community Center', 3),
(3, '2024-01-15', 'City Hall', 4),
(4, '2024-02-10', 'Bookstore', 1),
(5, '2024-03-20', 'Café', 5),
(6, '2024-04-18', 'University Auditorium', 6),
(7, '2024-05-30', 'Public Library', 7),
(8, '2024-06-25', 'Art Gallery', 8),
(9, '2024-07-12', 'Online (Zoom)', 9),
(10, '2024-08-19', 'Park Pavilion', 10);

INSERT INTO Reading_Sessions values (1, '2023-11-15', 'Thematic Discussion on 1984', 2),
(2, '2023-12-01', 'Character Analysis in Moby Dick', 3),
(3, '2024-01-10', 'Themes of To Kill a Mockingbird', 4),
(4, '2024-02-05', 'Exploring Pride and Prejudice', 5),
(5, '2024-03-15', 'Modern Relevance of The Great Gatsby', 6);

INSERT INTO Book_Requests values (1, 'The Catcher in the Rye', 'J.D. Salinger', 'Pending', 1),
(2, 'Brave New World', 'Aldous Huxley', 'Approved', 2),
(3, 'War and Peace', 'Leo Tolstoy', 'Rejected', 3),
(4, 'The Hobbit', 'J.R.R. Tolkien', 'Pending', 4),
(5, 'Fahrenheit 451', 'Ray Bradbury', 'Approved', 5);

INSERT INTO Book_Requests VALUES
(6, 'To the Lighthouse', 'Virginia Woolf', 'Approved', 1),
(7, 'Crime and Punishment', 'Fyodor Dostoevsky', 'Pending', 2),
(8, 'Don Quixote', 'Miguel de Cervantes', 'Rejected', 3),
(9, 'The Grapes of Wrath', 'John Steinbeck', 'Pending', 4),
(10, 'The Picture of Dorian Gray', 'Oscar Wilde', 'Approved', 5),
(11, 'Dracula', 'Bram Stoker', 'Rejected', 1),
(12, 'Wuthering Heights', 'Emily Brontë', 'Approved', 2),
(13, 'The Sound and the Fury', 'William Faulkner', 'Pending', 3),
(14, 'Slaughterhouse-Five', 'Kurt Vonnegut', 'Approved', 4),
(15, 'Catch-22', 'Joseph Heller', 'Rejected', 5),
(16, 'Moby-Dick', 'Herman Melville', 'Pending', 1),
(17, 'Beloved', 'Toni Morrison', 'Approved', 2),
(18, 'The Sun Also Rises', 'Ernest Hemingway', 'Pending', 3),
(19, 'The Stranger', 'Albert Camus', 'Rejected', 4),
(20, 'Lolita', 'Vladimir Nabokov', 'Approved', 5);


INSERT INTO Member_Attendance values (1, 1), 
(2, 1), 
(1, 2), 
(3, 3),  
(2, 3);  

INSERT INTO Disscusion_Topics values (1, 'The Significance of Dystopian Literature', '2023-11-30', 1),
(2, 'Character Development in Classic Novels', '2023-12-06', 2),
(3, 'Social Commentary in Modern Fiction', '2024-01-20', 3),
(4, 'The Role of Women in Literature', '2024-02-15', 4),
(5, 'Influence of Historical Events on Literature', '2024-03-25', 5);

INSERT INTO Roles values (1, 'Member', 1),
(2, 'Moderator', 2),
(3, 'Organizer', 3),
(4, 'Guest Speaker', 4),
(5, 'Volunteer', 5);

INSERT INTO Book_Recomandations values (1, 1, '2023-11-10'),  
(2, 2, '2023-11-15'), 
(3, 3, '2024-01-05'),  
(4, 4, '2024-01-20'), 
(5, 5, '2024-02-10');  

INSERT INTO Book_Reviews values (1, 5, 'A compelling read that challenges perceptions.', 2),
(2, 4, 'An exceptional portrayal of social issues.', 4),
(3, 3, 'A timeless classic that resonates today.', 1),
(4, 2, 'A thought-provoking narrative that captivates.', 5),
(5, 1, 'A brilliant exploration of humanity and power.', 3);


USE Book_Club3;
GO


-- 1. Classic SQL Injection on Members (first_name)

-- Vulnerable code:
DECLARE @input1 NVARCHAR(50);
--SET @input1 = 'Cristian'; 
SET @input1 = 'Cristian'' OR 1=1 --'; 

DECLARE @sql1 NVARCHAR(MAX);
SET @sql1 = 'SELECT * FROM Members WHERE first_name = ''' + @input1 + ''';';

-- vulnerable query
EXEC sp_executesql @sql1;


-- SOLUTION:
DECLARE @input1_safe NVARCHAR(50);
SET   @input1_safe = 'Cristian'' OR 1=1 --'; 

-- SAFE: parameterized query—input is treated as data, not code
DECLARE @sql1_safe NVARCHAR(MAX) = N'
    SELECT *
      FROM Members
     WHERE first_name = @firstname;
';

EXEC sp_executesql
     @sql1_safe,
     N'@firstname NVARCHAR(50)',
     @firstname = @input1_safe;
GO


-- 2. Union‐Based Injection on Books (genre)

-- Vulnerable code:
DECLARE @input2 NVARCHAR(100);
SET @input2 = 'Fantasy'; 

DECLARE @sql2 NVARCHAR(MAX);
SET @sql2 = 'SELECT title, author FROM Books WHERE genre = ''' + @input2 + ''';';

EXEC sp_executesql @sql2;

-- Malicious input example:

DECLARE @input2_mal NVARCHAR(100);
SET @input2_mal = ''' UNION SELECT CAST(members_id AS NVARCHAR(10)), email FROM Members --';

DECLARE @sql2_mal NVARCHAR(MAX);
SET @sql2_mal = 'SELECT title, author FROM Books WHERE genre = ''' + @input2_mal + ''';';

EXEC sp_executesql @sql2_mal;
GO

-- SOLUTION:
DECLARE @input2_safe NVARCHAR(100);
SET @input2_safe = ''' UNION SELECT CAST(members_id AS NVARCHAR(10)), email FROM Members --';

DECLARE @sql2_safe NVARCHAR(MAX) = N'
    SELECT title, author
      FROM Books
     WHERE genre = @genre;
';

EXEC sp_executesql
    @sql2_safe,
    N'@genre NVARCHAR(100)',
    @genre = @input2_safe;
GO


-- 3. LIKE Clause Injection on Book_Requests (book_title)

-- Vulnerable code:
DECLARE @input3 NVARCHAR(100);
SET  @input3 = 'The Hobbit'; 

DECLARE @sql3 NVARCHAR(MAX);
SET @sql3 = 'SELECT * FROM Book_Requests WHERE book_title LIKE ''%' + @input3 + '%'';';

EXEC sp_executesql @sql3;

-- Malicious input example:

DECLARE @input3_mal NVARCHAR(100);
SET @input3_mal = 'The Hobbit%'' OR 1=1 --';

DECLARE @sql3_mal NVARCHAR(MAX);
SET @sql3_mal = 'SELECT * FROM Book_Requests WHERE book_title LIKE ''%' + @input3_mal + '%'';';

EXEC sp_executesql @sql3_mal;
GO

-- SOLUTION:
DECLARE @input3_safe NVARCHAR(100);
SET  @input3_safe = 'The Hobbit%'' OR 1=1 --';  

DECLARE @pattern3 NVARCHAR(120);
SET  @pattern3 = '%' + @input3_safe + '%';

DECLARE @sql3_safe NVARCHAR(MAX);
SET @sql3_safe = N'
    SELECT *
      FROM Book_Requests
     WHERE book_title LIKE @searchTerm;
';

EXEC sp_executesql
    @sql3_safe,
    N'@searchTerm NVARCHAR(120)',
    @searchTerm = @pattern3;
GO



-- 4. Order By Clause Injection on Books (ordering by a column)

CREATE TABLE Book_Reviews (
	review_id int PRIMARY KEY,
	rating int,
	review_text varchar(70),
	fk_BookReviews_Books int FOREIGN KEY references Books(book_id),
)
INSERT INTO Book_Reviews values (1, 5, 'A compelling read that challenges perceptions.', 2),
(2, 4, 'An exceptional portrayal of social issues.', 4),
(3, 3, 'A timeless classic that resonates today.', 1),
(4, 2, 'A thought-provoking narrative that captivates.', 5),
(5, 1, 'A brilliant exploration of humanity and power.', 3);


-- Vulnerable code:
DECLARE @sortCol NVARCHAR(50);
SET @sortCol = 'title; DROP TABLE Book_Reviews; --';  -- Malicious input

DECLARE @sql4 NVARCHAR(MAX);
SET @sql4 = 'SELECT * FROM Books ORDER BY ' + @sortCol + ';';

EXEC sp_executesql @sql4;
-- Effect: Drops Book_Reviews table if permissions allow.

-- SOLUTION with Whitelisting:
DECLARE @sortCol_safe NVARCHAR(50);
SET @sortCol_safe = 'title; DROP TABLE Book_Reviews; --'; -- malicious

-- Whitelist allowed columns
IF @sortCol_safe NOT IN (N'book_id', N'title', N'author', N'genre', N'published_year')
BEGIN
    RAISERROR('Invalid sort column', 16, 1);
    RETURN;
END

DECLARE @sql4_safe NVARCHAR(MAX);
SET @sql4_safe = 'SELECT * FROM Books ORDER BY ' + QUOTENAME(@sortCol_safe) + ';';

EXEC sp_executesql @sql4_safe;
GO


-- 5. Stacked Queries Injection on Meetings (location_place)

CREATE TABLE Book_Recomandations (
	fk_BookRecomandations_Books int FOREIGN KEY references Books(book_id),
	fk_BookRecomandations_Members int FOREIGN KEY references Members(members_id),
	PRIMARY KEY (fk_BookRecomandations_Books, fk_BookRecomandations_Members),
	recomandation_date DATE
)

INSERT INTO Book_Recomandations values (1, 1, '2023-11-10'),  
(2, 2, '2023-11-15'), 
(3, 3, '2024-01-05'),  
(4, 4, '2024-01-20'), 
(5, 5, '2024-02-10');


-- Vulnerable code:
DECLARE @input5 NVARCHAR(100);
SET @input5 = 'Library';  -- Expected input

DECLARE @sql5 NVARCHAR(MAX);
SET @sql5 = 'SELECT * FROM Meetings WHERE location_place = ''' + @input5 + ''';';

EXEC sp_executesql @sql5;
GO

-- Malicious input example:
DECLARE @input5_mal NVARCHAR(100);
SET @input5_mal = 'Library''; DROP TABLE Book_Recomandations; --';

DECLARE @sql5_mal NVARCHAR(MAX);
SET @sql5_mal = 'SELECT * FROM Meetings WHERE location_place = ''' + @input5_mal + ''';';

EXEC sp_executesql @sql5_mal;
GO

-- SOLUTION:
DECLARE @input5_safe NVARCHAR(100);
SET @input5_safe = 'Library''; DROP TABLE Book_Recomandations; --'; 

DECLARE @sql5_safe NVARCHAR(MAX);
SET @sql5_safe = N'
    SELECT *
      FROM Meetings
     WHERE location_place = @loc;
';

EXEC sp_executesql
    @sql5_safe,
    N'@loc NVARCHAR(100)',
    @loc = @input5_safe;
GO
