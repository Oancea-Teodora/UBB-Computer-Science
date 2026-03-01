-- Create the database for task management
-- Run this as a separate script first:
-- CREATE DATABASE taskmanagement;
-- GO

USE taskmanagement;
GO

-- Create User table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='User' AND xtype='U')
CREATE TABLE [User] (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL
);
GO

-- Create Task table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Task' AND xtype='U')
CREATE TABLE Task (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    status NVARCHAR(20) CHECK (status IN ('todo', 'in_progress', 'done')) DEFAULT 'todo',
    assignedTo INT,
    lastUpdated DATETIME DEFAULT GETDATE(),
    lastUpdatedBy INT,
    FOREIGN KEY (assignedTo) REFERENCES [User](id),
    FOREIGN KEY (lastUpdatedBy) REFERENCES [User](id)
);
GO

-- Create TaskLog table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='TaskLog' AND xtype='U')
CREATE TABLE TaskLog (
    id INT IDENTITY(1,1) PRIMARY KEY,
    taskId INT NOT NULL,
    userId INT NOT NULL,
    oldStatus NVARCHAR(20),
    newStatus NVARCHAR(20),
    timestamp DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (taskId) REFERENCES Task(id),
    FOREIGN KEY (userId) REFERENCES [User](id)
);
GO

-- Insert test users
IF NOT EXISTS (SELECT * FROM [User] WHERE username = 'alice')
INSERT INTO [User] (username) VALUES ('alice');
IF NOT EXISTS (SELECT * FROM [User] WHERE username = 'bob')
INSERT INTO [User] (username) VALUES ('bob');
IF NOT EXISTS (SELECT * FROM [User] WHERE username = 'charlie')
INSERT INTO [User] (username) VALUES ('charlie');
IF NOT EXISTS (SELECT * FROM [User] WHERE username = 'diana')
INSERT INTO [User] (username) VALUES ('diana');
GO

-- Insert test tasks
IF NOT EXISTS (SELECT * FROM Task WHERE title = 'Setup project environment')
INSERT INTO Task (title, status, assignedTo, lastUpdatedBy) VALUES 
('Setup project environment', 'todo', 1, 1);
IF NOT EXISTS (SELECT * FROM Task WHERE title = 'Create database schema')
INSERT INTO Task (title, status, assignedTo, lastUpdatedBy) VALUES 
('Create database schema', 'in_progress', 2, 2);
IF NOT EXISTS (SELECT * FROM Task WHERE title = 'Implement user authentication')
INSERT INTO Task (title, status, assignedTo, lastUpdatedBy) VALUES 
('Implement user authentication', 'todo', 1, 1);
IF NOT EXISTS (SELECT * FROM Task WHERE title = 'Design task board UI')
INSERT INTO Task (title, status, assignedTo, lastUpdatedBy) VALUES 
('Design task board UI', 'done', 3, 3);
IF NOT EXISTS (SELECT * FROM Task WHERE title = 'Add task moving functionality')
INSERT INTO Task (title, status, assignedTo, lastUpdatedBy) VALUES 
('Add task moving functionality', 'in_progress', 2, 2);
IF NOT EXISTS (SELECT * FROM Task WHERE title = 'Test application')
INSERT INTO Task (title, status, assignedTo, lastUpdatedBy) VALUES 
('Test application', 'todo', 4, 4);
GO

-- Show the setup
SELECT 'Users table:' as Info;
SELECT * FROM [User];
SELECT 'Tasks table:' as Info;
SELECT * FROM Task;
SELECT 'TaskLog table:' as Info;
SELECT * FROM TaskLog; 