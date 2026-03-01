-- ============================================
-- 1) Dirty Read Example (Generic TableName)
-- ============================================

-- Assume a table:
--   CREATE TABLE TableName (
--     column_int   INT   PRIMARY KEY,
--     column_char  CHAR(20)
--   );
-- And there's at least one row where column_int = 1.

-- === Transaction A: makes a change but eventually rolls back ===
BEGIN TRAN;  
    -- A updates column_char for the row where column_int = 1
    UPDATE TableName
      SET column_char = 'NEWVALUE'
    WHERE column_int = 1;

    -- Pause to simulate long operation
    WAITFOR DELAY '00:00:10';

    -- Rollback the change—so in reality nothing persists
    ROLLBACK;
-- Transaction A ends here (no commit, change is undone).


-- === Transaction B: reads under READ UNCOMMITTED (dirty read) ===
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
BEGIN TRAN;
    -- B reads the same row immediately after A’s UPDATE, before A rolls back.
    SELECT column_int, column_char
      FROM TableName
      WHERE column_int = 1;
    -- This returns 'NEWVALUE' even though A will rollback later.

    WAITFOR DELAY '00:00:15';

    -- B reads again (but by now A’s rollback has occurred)
    SELECT column_int, column_char
      FROM TableName
      WHERE column_int = 1;
COMMIT;
-- Under READ UNCOMMITTED, the first SELECT “sees” the uncommitted change (dirty read).


-- === Dirty-Read Fix: use READ COMMITTED ===
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN TRAN;
    -- Now B’s first read will block until A either rolls back or commits.
    SELECT column_int, column_char
      FROM TableName
      WHERE column_int = 1;

    WAITFOR DELAY '00:00:15';

    SELECT column_int, column_char
      FROM TableName
      WHERE column_int = 1;
COMMIT;

-- Under READ COMMITTED, B never sees the uncommitted 'NEWVALUE'.




-- ===================================================
-- 2) Non-Repeatable Read Example (Generic TableName)
-- ===================================================

-- Assume TableName exists as before, and currently has no row with column_char = 'TempRow'.

-- === Transaction A: inserts a row, waits, then updates it ===
-- (A eventually commits)
INSERT INTO TableName (column_int, column_char)
SELECT ISNULL(MAX(column_int), 0) + 1, 'TempRow'
  FROM TableName;
-- At this point A has inserted, but we don’t see it until commit.

BEGIN TRAN;
    -- Let B run its first SELECT after insertion completes
    WAITFOR DELAY '00:00:05';

    -- Now A updates that same new row’s column_char to a different value
    UPDATE TableName
      SET column_char = 'UpdatedTemp'
    WHERE column_char = 'TempRow';

    COMMIT;
-- A’s net effect: one row inserted, immediately followed by changing column_char to 'UpdatedTemp'.


-- === Transaction B: running under READ COMMITTED ===
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN TRAN;
    -- B’s first read: finds the newly inserted row (since A’s INSERT was already committed).
    SELECT column_int, column_char
      FROM TableName
      WHERE column_char = 'TempRow';
    -- At this moment, before WAITFOR, column_char = 'TempRow'.

    WAITFOR DELAY '00:00:05';
    -- During this delay, Transaction A ran its UPDATE and committed,
    -- so now the row’s column_char = 'UpdatedTemp'.

    -- B’s second read: the same predicate still matches (column_char='TempRow'? Actually A changed it to 'UpdatedTemp').
    SELECT column_int, column_char
      FROM TableName
      WHERE column_char = 'TempRow';
    -- Now this returns 0 rows (or different data) because A’s UPDATE changed the value.
COMMIT;


-- === Non-Repeatable-Read Fix: use REPEATABLE READ ===
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
BEGIN TRAN;
    -- Under REPEATABLE READ, once B’s first SELECT sees the row,
    -- the lock on that row’s data (for column_char) is held until B commits,
    -- preventing A from changing it until B finishes.
    SELECT column_int, column_char
      FROM TableName
      WHERE column_char = 'TempRow';

    WAITFOR DELAY '00:00:05';

    -- Because B holds a shared lock on the matching row, A’s UPDATE must wait
    -- until B commits or rolls back. Thus when this SELECT runs, it sees the same value.
    SELECT column_int, column_char
      FROM TableName
      WHERE column_char = 'TempRow';
COMMIT;






-- =======================================================
-- 3) Phantom Read Example (Generic TableName with column_year)
-- =======================================================

-- Assume TableName now has:
--   column_int   INT   PRIMARY KEY,
--   column_char  CHAR(20),
--   column_year  CHAR(4)

-- And initially there are, say, N rows where column_year = '2025'.

-- === Transaction A: after a short delay, inserts a new '2025' row, then commits ===
BEGIN TRAN;
    WAITFOR DELAY '00:00:04';
    INSERT INTO TableName (column_int, column_char, column_year)
    SELECT ISNULL(MAX(column_int),0) + 1, 'NewPhantom', '2025'
      FROM TableName;
COMMIT;

-- === Transaction B: under REPEATABLE READ, counting rows with column_year = '2025' ===
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
BEGIN TRAN;
    -- First count: suppose it returns N.
    SELECT COUNT(*) AS Year2025Count
      FROM TableName
      WHERE column_year = '2025';

    WAITFOR DELAY '00:00:05';
    -- During this pause, Transaction A will insert a new row matching column_year='2025'.

    -- Second count: now returns N+1 (a “phantom row” appeared).
    SELECT COUNT(*) AS Year2025Count
      FROM TableName
      WHERE column_year = '2025';
COMMIT;

-- === Phantom-Read Fix: use SERIALIZABLE ===
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN TRAN;
    -- Under SERIALIZABLE, the range "WHERE column_year = '2025'" is locked so no new row matching that predicate can be inserted.
    SELECT COUNT(*) AS Year2025Count
      FROM TableName
      WHERE column_year = '2025';

    WAITFOR DELAY '00:00:05';
    -- A’s INSERT must wait until B commits, so B’s second COUNT returns the same value N.
    SELECT COUNT(*) AS Year2025Count
      FROM TableName
      WHERE column_year = '2025';
COMMIT;





-- =========================================================
-- 4) Deadlock Example (Generic TableA and TableB)
-- =========================================================

-- Assume:
--   CREATE TABLE TableA (
--     column_int   INT   PRIMARY KEY,
--     column_char  CHAR(20)
--   );
--   CREATE TABLE TableB (
--     column_int   INT   PRIMARY KEY,
--     column_char  CHAR(20)
--   );
-- And each table has at least one row with column_int = 1.

-- === Transaction A: updates TableA first, then TableB ===
BEGIN TRAN;  
    -- A locks the row in TableA where column_int = 1
    UPDATE TableA
      SET column_char = 'A_UpdatedA'
    WHERE column_int = 1;

    WAITFOR DELAY '00:00:10';
    -- After delay, A then tries to lock TableB’s row
    UPDATE TableB
      SET column_char = 'A_UpdatedB'
    WHERE column_int = 1;

COMMIT;


-- === Transaction B: updates TableB first, then TableA ===
BEGIN TRAN;  
    -- B locks the row in TableB where column_int = 1
    UPDATE TableB
      SET column_char = 'B_UpdatedB'
    WHERE column_int = 1;

    WAITFOR DELAY '00:00:10';
    -- After delay, B tries to lock TableA’s row
    UPDATE TableA
      SET column_char = 'B_UpdatedA'
    WHERE column_int = 1;

COMMIT;


-- If A and B start at roughly the same time, the following can happen:
--   • A holds a lock on TableA/row(1) and waits on TableB/row(1).
--   • B holds a lock on TableB/row(1) and waits on TableA/row(1).
--   → Deadlock! One transaction is chosen as the victim and rolled back.


-- === Deadlock Fix: enforce a consistent locking order ===
-- For example, both transactions must update TableA first, then TableB.

-- Fixed Transaction A (always TableA → TableB)
BEGIN TRAN;
    UPDATE TableA
      SET column_char = 'FixA_A'
    WHERE column_int = 1;

    WAITFOR DELAY '00:00:10';

    UPDATE TableB
      SET column_char = 'FixA_B'
    WHERE column_int = 1;
COMMIT;


-- Fixed Transaction B (also TableA → TableB)
BEGIN TRAN;
    -- Even though B logically “wants” to update TableB first, it must respect the global order.
    -- So B first updates TableA (acquiring lock on the same row A locked).
    UPDATE TableA
      SET column_char = 'FixB_A'
    WHERE column_int = 1;

    WAITFOR DELAY '00:00:10';

    UPDATE TableB
      SET column_char = 'FixB_B'
    WHERE column_int = 1;
COMMIT;

-- Now both A and B request locks on TableA first, then TableB.
-- Even if they overlap in time, they queue up on the same lock acquisition order,
-- so no cyclic wait can occur → no deadlock.
