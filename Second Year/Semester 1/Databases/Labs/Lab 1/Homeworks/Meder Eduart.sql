--1. User Table
CREATE TABLE [User] (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
    user_type VARCHAR(20) CHECK (user_type IN ('broker', 'investor')));

--2. Broker Table
CREATE TABLE Broker (
    broker_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT,
    license_no VARCHAR(50),
    firm_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES [User](user_id) ON DELETE CASCADE
);

--3. Investor Table (No cascade actions on user_id and broker_id)
CREATE TABLE Investor (
    investor_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT,
    broker_id INT,
    risk_profile VARCHAR(20) CHECK (risk_profile IN ('low', 'medium', 'high')),
    FOREIGN KEY (user_id) REFERENCES [User](user_id) ON DELETE NO ACTION,  
    FOREIGN KEY (broker_id) REFERENCES Broker(broker_id) ON DELETE NO ACTION 
);

--4. Account Table
CREATE TABLE Account (
    account_id INT IDENTITY(1,1) PRIMARY KEY,
    investor_id INT,
    account_type VARCHAR(20) CHECK (account_type IN ('checking', 'savings', 'brokerage')),
    balance DECIMAL(15, 2),
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id) ON DELETE CASCADE
);

--5. Portfolio Table
CREATE TABLE Portfolio (
    portfolio_id INT IDENTITY(1,1) PRIMARY KEY,
    investor_id INT,
    portfolio_name VARCHAR(100),
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id) ON DELETE CASCADE
);

--6. Security Table
CREATE TABLE Security (
    security_id INT IDENTITY(1,1) PRIMARY KEY,
    security_type VARCHAR(20) CHECK (security_type IN ('stock', 'bond', 'ETF', 'mutual fund')),
    ticker_symbol VARCHAR(10) UNIQUE,
    name VARCHAR(100),
    price DECIMAL(15, 2)
);

--7. Trade Table (Modified to avoid multiple cascade paths)
CREATE TABLE Trade (
    trade_id INT IDENTITY(1,1) PRIMARY KEY,
    investor_id INT,
    security_id INT,
    account_id INT,
    trade_type VARCHAR(20) CHECK (trade_type IN ('buy', 'sell')),
    quantity INT,
    trade_date DATETIME,
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id) ON DELETE CASCADE,     
    FOREIGN KEY (security_id) REFERENCES Security(security_id) ON DELETE CASCADE,    
    FOREIGN KEY (account_id) REFERENCES Account(account_id) ON DELETE NO ACTION        
);

--8. Transaction Table
CREATE TABLE [Transaction] (
    transaction_id INT IDENTITY(1,1) PRIMARY KEY,
    account_id INT,
    amount DECIMAL(15, 2),
    transaction_type VARCHAR(20) CHECK (transaction_type IN ('deposit', 'withdrawal')),
    transaction_date DATETIME,
    FOREIGN KEY (account_id) REFERENCES Account(account_id) ON DELETE CASCADE
);

--9. MarketData Table
CREATE TABLE MarketData (
    market_data_id INT IDENTITY(1,1) PRIMARY KEY,
    security_id INT,
    price DECIMAL(15, 2),
    recorded_at DATETIME,
    FOREIGN KEY (security_id) REFERENCES Security(security_id) ON DELETE CASCADE
);

--10. Order Table
CREATE TABLE [Order] (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    investor_id INT,
    security_id INT,
    order_type VARCHAR(20) CHECK (order_type IN ('market', 'limit', 'stop')),
    quantity INT,
    order_date DATETIME,
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id) ON DELETE CASCADE,
    FOREIGN KEY (security_id) REFERENCES Security(security_id) ON DELETE CASCADE
);

--11. Investor_Security Table (Many-to-Many Relationship between Investor and Security)
CREATE TABLE Investor_Security (
    investor_id INT,
    security_id INT,
    quantity INT,
    PRIMARY KEY (investor_id, security_id),
    FOREIGN KEY (investor_id) REFERENCES Investor(investor_id) ON DELETE CASCADE,
    FOREIGN KEY (security_id) REFERENCES Security(security_id) ON DELETE CASCADE
);

--12. Broker_Security Table (Many-to-Many Relationship between Broker and Security)
CREATE TABLE Broker_Security (
    broker_id INT,
    security_id INT,
    PRIMARY KEY (broker_id, security_id),
    FOREIGN KEY (broker_id) REFERENCES Broker(broker_id) ON DELETE CASCADE,
    FOREIGN KEY (security_id) REFERENCES Security(security_id) ON DELETE CASCADE
);
