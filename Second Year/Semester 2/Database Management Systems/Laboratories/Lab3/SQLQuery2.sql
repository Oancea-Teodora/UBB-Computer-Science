use Book_Club


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
  WHERE title = 'Temp Read';   -- sees Adventure
WAITFOR DELAY '00:00:05';
SELECT title, genre
  FROM Books
  WHERE title = 'Temp Read';   -- seesClassic
COMMIT;

--  Non-repeatable fix
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
BEGIN TRAN;
SELECT title, genre
  FROM Books
  WHERE title = 'Temp Read';   -- sees Adventure
WAITFOR DELAY '00:00:05';
SELECT title, genre
  FROM Books
  WHERE title = 'Temp Read';   -- still Adventure
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

ALTER DATABASE Book_Club SET ALLOW_SNAPSHOT_ISOLATION ON;
ALTER DATABASE Book_Club SET READ_COMMITTED_SNAPSHOT ON;
GO

--Transaction A
USE Book_Club;
GO
BEGIN TRANSACTION;
  -- Update the same row that B will try to update
  UPDATE Books
    SET genre = 'Alternate'
  WHERE book_id = 2;   -- originally Dystopian
  PRINT 'Transaction A: holding lock for 10s...';
  WAITFOR DELAY '00:00:10';
COMMIT TRANSACTION;
PRINT 'Transaction A: committed';

--Transaction B
USE Book_Club;
GO
SET TRANSACTION ISOLATION LEVEL SNAPSHOT;
GO
BEGIN TRANSACTION;
  SELECT book_id, title, genre   --seed Dystopian
    FROM Books
    WHERE book_id = 2;
  WAITFOR DELAY '00:00:15';
  --Attempt to update same row
  UPDATE Books
    SET genre = 'Speculative'
  WHERE book_id = 2;
COMMIT TRANSACTION;
