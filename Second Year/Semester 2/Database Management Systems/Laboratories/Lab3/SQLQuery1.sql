-- GRADE 3;
-- create a stored procedure that inserts data in tables that are in a m:n relationship; 
-- if one insert fails, all the operations performed by the procedure must be rolled back

-- LOG TABLE
 CREATE TABLE LogTable (
        Lid INT IDENTITY PRIMARY KEY,
        TypeOperation VARCHAR(50),
        TableOperation VARCHAR(50),
        ExecutionDate DATETIME);
GO

-- Validation Functions
-- 1.1 Length
CREATE OR ALTER FUNCTION uf_ValidateLength70(@s VARCHAR(70))
RETURNS BIT
AS
BEGIN
    RETURN CASE 
        WHEN @s IS NOT NULL AND LEN(@s) BETWEEN 1 AND 70 
        THEN 1 ELSE 0 
    END;
END;
GO

-- 1.2 Genre 
CREATE OR ALTER FUNCTION uf_ValidateGenre(@g VARCHAR(70))
RETURNS BIT
AS
BEGIN
    RETURN CASE 
        WHEN @g IN (
            'Fictiune istorica','Dystopian','Adventure',
            'Fiction','Classic','Romance',
            'Historical Fiction','Fantasy'
        ) THEN 1 
        ELSE 0 
    END;
END;
GO

-- 1.3 PublishedYear as text must cast to integer between 1500 and current year
CREATE OR ALTER FUNCTION uf_ValidatePublishedYear(@yrText VARCHAR(70))
RETURNS BIT
AS
BEGIN
    DECLARE @y INT = TRY_CAST(@yrText AS INT);
    RETURN CASE 
        WHEN @y IS NOT NULL 
          AND @y BETWEEN 1500 AND YEAR(GETDATE()) 
        THEN 1 ELSE 0 
    END;
END;
GO

-- 1.4 Name must be 1–70 chars, start uppercase, letters/spaces only
CREATE OR ALTER FUNCTION uf_ValidateName(@n VARCHAR(70))
RETURNS BIT
AS
BEGIN
    RETURN CASE 
        WHEN dbo.uf_ValidateLength70(@n)=1
         AND @n LIKE '[A-Z]%' 
         AND @n NOT LIKE '%[^A-Za-z ]%' 
        THEN 1 ELSE 0 
    END;
END;
GO

-- 1.5 Email must be 1–70 chars and contain an '@' and a '.'
CREATE OR ALTER FUNCTION uf_ValidateEmail(@e VARCHAR(70))
RETURNS BIT
AS
BEGIN
    RETURN CASE 
        WHEN dbo.uf_ValidateLength70(@e)=1
         AND CHARINDEX('@',@e) > 1
         AND CHARINDEX('.',@e,CHARINDEX('@',@e)+2) > CHARINDEX('@',@e)
        THEN 1 ELSE 0 
    END;
END;
GO

-- 1.6 JoinYear must be between 1900 and current year
CREATE OR ALTER FUNCTION uf_ValidateJoinYear(@yr INT)
RETURNS BIT
AS
BEGIN
    RETURN CASE 
        WHEN @yr BETWEEN 1900 AND YEAR(GETDATE()) 
        THEN 1 ELSE 0 
    END;
END;
GO

CREATE OR ALTER PROCEDURE dbo.AddRecommendation_AllOrNothing
    @title               VARCHAR(70),
    @author              VARCHAR(70),
    @genre               VARCHAR(70),
    @published_year      VARCHAR(70),
    @first_name          VARCHAR(70),
    @second_name         VARCHAR(70),
    @email               VARCHAR(70),
    @join_year           INT,
    @recommendation_date DATE
AS
BEGIN
    SET XACT_ABORT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validate all inputs
        IF dbo.uf_ValidateLength70(@title)=0
            THROW 71000, 'Title must be 1–70 chars.', 1;
        IF dbo.uf_ValidateName(@author)=0
            THROW 71001, 'Author must start uppercase, letters/spaces only.', 1;
        IF dbo.uf_ValidateGenre(@genre)=0
            THROW 71002, 'Genre invalid.', 1;
        IF dbo.uf_ValidatePublishedYear(@published_year)=0
            THROW 71003, 'Published_year invalid.', 1;
        IF dbo.uf_ValidateName(@first_name)=0
            THROW 71004, 'First_name invalid.', 1;
        IF dbo.uf_ValidateName(@second_name)=0
            THROW 71005, 'Second_name invalid.', 1;
        IF dbo.uf_ValidateEmail(@email)=0
            THROW 71006, 'Email invalid.', 1;
        IF dbo.uf_ValidateJoinYear(@join_year)=0
            THROW 71007, 'Join_year invalid.', 1;
        IF @recommendation_date IS NULL
            THROW 71008, 'Recommendation_date is required.', 1;

        -- Insert into Books
        DECLARE @BookID INT = (SELECT ISNULL(MAX(book_id),0)+1 FROM Books);
        INSERT INTO Books(book_id, title, author, genre, published_year)
        VALUES (@BookID, @title, @author, @genre, @published_year);

        -- Insert into Members
        DECLARE @MemberID INT = (SELECT ISNULL(MAX(members_id),0)+1 FROM Members);
        INSERT INTO Members(members_id, first_name, second_name, email, join_year)
        VALUES (@MemberID, @first_name, @second_name, @email, @join_year);

        -- Insert in Book_Recomandations
        INSERT INTO Book_Recomandations
            (fk_BookRecomandations_Books, fk_BookRecomandations_Members, recomandation_date)
        VALUES
            (@BookID, @MemberID, @recommendation_date);

        -- Log success
        INSERT INTO LogTable(TypeOperation, TableOperation)
        VALUES('INSERT','Books|Members|Book_Recomandations');

        COMMIT;
    END TRY
    BEGIN CATCH
        -- Roll back on any error
        IF @@TRANCOUNT > 0
            ROLLBACK;
        -- Log the rollback
        INSERT INTO LogTable(TypeOperation, TableOperation)
        VALUES('ROLLBACK','AddRecommendation_AllOrNothing');
        THROW;
    END CATCH; 
END;
GO

-- TESTS
DELETE LogTable

SELECT * FROM Books;
SELECT * FROM Members;
SELECT * FROM Book_Recomandations;

-- success case
EXEC dbo.AddRecommendation_AllOrNothing
    '1984','George Orwell','Dystopian','1949',
    'Alice','Smith','alice@example.com',2025,'2025-05-14';

-- failure case
EXEC dbo.AddRecommendation_AllOrNothing
    'Brave New World','Aldous Huxley','Sci-Fi','1932',
    NULL,'Adams','carol@example.com',2025,'2025-05-14';

SELECT * FROM LogTable
GO

---------------------------------------------------------------------
---------------------------------------------------------------------

-- GRADE 5
-- create a stored procedure that inserts data in tables that are in a m:n relationship; 
-- if an insert fails, try to recover as much as possible from the entire operation: 
-- for example, if the user wants to add a book and its authors, succeeds creating the authors, 
-- but fails with the book, the authors should remain in the database

CREATE OR ALTER PROCEDURE AddRecommendation_PartialRecover
    @title               VARCHAR(70),
    @author              VARCHAR(70),
    @genre               VARCHAR(70),
    @published_year      VARCHAR(70),
    @first_name          VARCHAR(70),
    @second_name         VARCHAR(70),
    @email               VARCHAR(70),
    @join_year           INT,
    @recommendation_date DATE
AS
BEGIN
    DECLARE 
        @BookID INT = NULL,
        @MemberID INT = NULL;

    DECLARE @History TABLE (
        Step VARCHAR(50),
        Outcome VARCHAR(200)
    );

    -- Insert into Books 
    BEGIN TRY
        BEGIN TRANSACTION;
        SET XACT_ABORT ON;

        IF dbo.uf_ValidateLength70(@title)=0 
            THROW 52001, 'Title must be 1–70 chars.', 1;
        IF dbo.uf_ValidateName(@author)=0 
            THROW 52002, 'Author invalid.', 1;
        IF dbo.uf_ValidateGenre(@genre)=0 
            THROW 52003, 'Genre invalid.', 1;
        IF dbo.uf_ValidatePublishedYear(@published_year)=0 
            THROW 52004, 'Published year invalid.', 1;

        SELECT @BookID = COALESCE(MAX(book_id),0)+1 FROM Books;
        INSERT INTO Books(book_id, title, author, genre, published_year)
         VALUES(@BookID, @title, @author, @genre, @published_year);

        COMMIT;

        INSERT INTO @History VALUES('Books','Inserted book_id=' + CAST(@BookID AS VARCHAR(10)));
        INSERT INTO LogTable(TypeOperation, TableOperation, ExecutionDate)
         VALUES('INSERT','Books', GETDATE());
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT>0 ROLLBACK;
        INSERT INTO @History VALUES('Books','Failed: ' + ERROR_MESSAGE());
        INSERT INTO LogTable(TypeOperation, TableOperation, ExecutionDate)
         VALUES('ERROR','Books', GETDATE());
    END CATCH;

    -- Insert into Members 
    BEGIN TRY
        BEGIN TRANSACTION;
        SET XACT_ABORT ON;

        IF dbo.uf_ValidateName(@first_name)=0 
            THROW 52011, 'First_name invalid.', 1;
        IF dbo.uf_ValidateName(@second_name)=0 
            THROW 52012, 'Second_name invalid.', 1;
        IF dbo.uf_ValidateEmail(@email)=0 
            THROW 52013, 'Email invalid.', 1;
        IF dbo.uf_ValidateJoinYear(@join_year)=0 
            THROW 52014, 'Join_year invalid.', 1;

        SELECT @MemberID = COALESCE(MAX(members_id),0)+1 FROM Members;
        INSERT INTO Members(members_id, first_name, second_name, email, join_year)
         VALUES(@MemberID, @first_name, @second_name, @email, @join_year);

        COMMIT;

        INSERT INTO @History VALUES('Members','Inserted members_id=' + CAST(@MemberID AS VARCHAR(10)));
        INSERT INTO LogTable(TypeOperation, TableOperation, ExecutionDate)
         VALUES('INSERT','Members', GETDATE());
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT>0 ROLLBACK;
        INSERT INTO @History VALUES('Members','Failed: ' + ERROR_MESSAGE());
        INSERT INTO LogTable(TypeOperation, TableOperation, ExecutionDate)
         VALUES('ERROR','Members', GETDATE());
    END CATCH;

    -- Insert into Book_Recomandations
    BEGIN TRY
        BEGIN TRANSACTION;
        SET XACT_ABORT ON;

        IF @BookID IS NULL OR @MemberID IS NULL
            THROW 52020, 'Cannot link: missing BookID or MemberID.', 1;

        INSERT INTO Book_Recomandations
         (fk_BookRecomandations_Books, fk_BookRecomandations_Members, recomandation_date)
         VALUES(@BookID, @MemberID, @recommendation_date);

        COMMIT;

        INSERT INTO @History VALUES('Recommendation','Linked B=' + CAST(@BookID AS VARCHAR(10))
                                               + ' M=' + CAST(@MemberID AS VARCHAR(10)));
        INSERT INTO LogTable(TypeOperation, TableOperation, ExecutionDate)
         VALUES('INSERT','Book_Recomandations', GETDATE());
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT>0 ROLLBACK;
        INSERT INTO @History VALUES('Recommendation','Failed: ' + ERROR_MESSAGE());
        INSERT INTO LogTable(TypeOperation, TableOperation, ExecutionDate)
         VALUES('ERROR','Book_Recomandations', GETDATE());
    END CATCH;

    SELECT * FROM @History;
END;
GO

-- TESTS


SELECT * FROM Books;
SELECT * FROM Members;
SELECT * FROM Book_Recomandations;

-- SUCCESS CASE
EXEC AddRecommendation_PartialRecover 'The Alchemist', 'Paulo Coelho', 'Fiction',  '1988',  'Ioana',  
'Constantin',  'ioana.constantin@hotmail.com',  2020,  '2025-05-15';
GO

-- FAILURE CASE
EXEC AddRecommendation_PartialRecover
   'Invisible Cities',  'Italo Calvino', 'Classic', '1972', 'Diana',  'Mihailescu',
   'diana.mihailescu_at_gmail.com',    2021,  '2025-05-16';
GO

SELECT * FROM LogTable
GO

-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------

-- create 4 scenarios that reproduce the following concurrency issues under pessimistic isolation 
-- levels: dirty reads, non-repeatable reads, phantom reads, and a deadlock; you can use stored 
-- procedures and / or stand-alone queries; find solutions to solve / workaround the concurrency 
-- issues (grade 9); 


-- 1) Dirty Read
-- Transaction A
BEGIN TRAN;
UPDATE Books
  SET genre = 'Suspense'
WHERE book_id = 7;     
WAITFOR DELAY '00:00:10';
ROLLBACK;

-- Transaction B
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
BEGIN TRAN;
SELECT book_id, title, genre
  FROM Books
  WHERE book_id = 7;    
WAITFOR DELAY '00:00:15';
SELECT book_id, title, genre
  FROM Books
  WHERE book_id = 7;   
COMMIT;


--Dirty-read fix
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN TRAN;
SELECT book_id, title, genre
  FROM Books
  WHERE book_id = 7;    
WAITFOR DELAY '00:00:15';
SELECT book_id, title, genre
  FROM Books
  WHERE book_id = 7;    
COMMIT;


-- 2) Non-Repeatable Read

-- Transaction A
-- Insert then update, committed after a delay
INSERT INTO Books (book_id, title, author, genre, published_year)
SELECT MAX(book_id)+1, 'Temp Read', 'Anon', 'Adventure', '2000'
  FROM Books;
BEGIN TRAN;
WAITFOR DELAY '00:00:05';
UPDATE Books
  SET genre = 'Classic'
  WHERE title = 'Temp Read';
COMMIT;

-- Transaction B
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN TRAN;
SELECT title, genre
  FROM Books
  WHERE title = 'Temp Read';   
WAITFOR DELAY '00:00:05';
SELECT title, genre
  FROM Books
  WHERE title = 'Temp Read';   
COMMIT;

--  Non-repeatable fix
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
BEGIN TRAN;
SELECT title, genre
  FROM Books
  WHERE title = 'Temp Read';  
WAITFOR DELAY '00:00:05';
SELECT title, genre
  FROM Books
  WHERE title = 'Temp Read';   
COMMIT;

-- 3) Phantom Read

-- Transaction A 
BEGIN TRAN;
WAITFOR DELAY '00:00:04';
INSERT INTO Books (book_id, title, author, genre, published_year)
SELECT MAX(book_id)+1, 'Phantom Book', 'Anon', 'Fantasy', '2025'
  FROM Books;
COMMIT;

-- Transaction B
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
BEGIN TRAN;
SELECT COUNT(*) AS Count2025
  FROM Books
  WHERE published_year = '2025';  
WAITFOR DELAY '00:00:05';
SELECT COUNT(*) AS Count2025
  FROM Books
  WHERE published_year = '2025';  
COMMIT;

-- Phantom-read fix
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN TRAN;
SELECT COUNT(*) AS Count2025
  FROM Books
  WHERE published_year = '2025';   
WAITFOR DELAY '00:00:05';
SELECT COUNT(*) AS Count2025
  FROM Books
  WHERE published_year = '2025';  
COMMIT;

-- 4) Deadlock

--Transaction A
BEGIN TRAN;
UPDATE Books
  SET title = 'T1 Update'
  WHERE book_id = 1;
WAITFOR DELAY '00:00:10';
UPDATE Members
  SET first_name = 'T1Name'
  WHERE members_id = 1;
COMMIT;

-- Transaction B
BEGIN TRAN;
UPDATE Members
  SET first_name = 'T2Name'
  WHERE members_id = 1;
WAITFOR DELAY '00:00:10';
UPDATE Books
  SET title = 'T2 Update'
  WHERE book_id = 1;
COMMIT;

-- Deadlock fix 
USE Book_Club;
GO

--Transaction A
BEGIN TRANSACTION;
  UPDATE Books
    SET title = 'Resolved T1 Books'
  WHERE book_id = 20;
  WAITFOR DELAY '00:00:10';
  UPDATE Members
    SET first_name = 'Resolved T1 Members'
  WHERE members_id = 6;
COMMIT TRANSACTION;

--Transaction B
BEGIN TRANSACTION;
  UPDATE Books
    SET title = 'Resolved T2 Books'
  WHERE book_id = 20;
  WAITFOR DELAY '00:00:10';
  UPDATE Members
    SET first_name = 'Resolved T2 Members'
  WHERE members_id = 6;
COMMIT TRANSACTION;


---------------------------------------------------------------------
---------------------------------------------------------------------

 
--create a scenario that reproduces the update conflict under an optimistic 
--isolation level (grade 10).  
/*ALTER DATABASE Book_Club 
  SET SINGLE_USER 
  WITH ROLLBACK IMMEDIATE;

ALTER DATABASE Book_Club
  SET MULTI_USER;
GO*/

ALTER DATABASE Book_Club SET ALLOW_SNAPSHOT_ISOLATION ON;
GO

--Transaction A
USE Book_Club;
GO
BEGIN TRANSACTION;
  UPDATE Books
    SET genre = 'Alternate'
  WHERE book_id = 2;   
  WAITFOR DELAY '00:00:10';
COMMIT TRANSACTION;


--Transaction B
USE Book_Club;
GO
SET TRANSACTION ISOLATION LEVEL SNAPSHOT;
GO
BEGIN TRANSACTION;
  SELECT book_id, title, genre
    FROM Books
    WHERE book_id = 2;
  WAITFOR DELAY '00:00:15';
  UPDATE Books
    SET genre = 'Speculative'
  WHERE book_id = 2;
COMMIT TRANSACTION;

ALTER DATABASE Book_Club SET ALLOW_SNAPSHOT_ISOLATION OFF 