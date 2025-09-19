Create database TeaShop
go
use TeaShop
go

CREATE TABLE TeaLeaves
(Lid INT PRIMARY KEY IDENTITY(1,1),
Name varchar(50),
Colour varchar(50),
Origin varchar(50),
Quantity bigint check (Quantity > 0) default(1))

CREATE TABLE TeaBlend
(Bid INT PRIMARY KEY IDENTITY(1,1),
Name varchar(50),
InfusionTime float check (InfusionTime > 1.0) default(1),
NoOfTeas int check (NoOfTeas > 0) default(1),
PricePerKG float check (PricePerKG > 0.0) default(1))

CREATE TABLE LeavesBlend
(Lid INT FOREIGN KEY REFERENCES TeaLeaves(Lid) on delete cascade,
Bid INT FOREIGN KEY REFERENCES TeaBlend(Bid) on delete cascade,
LBid int primary key identity(1,1))

CREATE TABLE TeaPacket
(Pid INT PRIMARY KEY IDENTITY(1,1),
Weight float check (Weight > 0.0) default(1),
Price float check (Price > 0.0) default(1),
Bid INT FOREIGN KEY REFERENCES TeaBlend(Bid) on delete cascade)

CREATE TABLE Address
(Aid INT PRIMARY KEY IDENTITY,
Street varchar(50),
City varchar(50),
County varchar(50),
PostalCode int)

CREATE TABLE Customer
(Cid INT PRIMARY KEY IDENTITY,
FirstName varchar(50),
LastName varchar(50),
Telephone int,
Email varchar(50),
Aid INT FOREIGN KEY REFERENCES Address(Aid) on delete cascade)

CREATE TABLE TimeDate
(Tid INT PRIMARY KEY IDENTITY,
Hour int,
Minute int,
Second int,
Day int,
Month int,
Year int)

CREATE TABLE Discount
(Did INT PRIMARY KEY IDENTITY,
Percentage int,
Code varchar(10))

CREATE TABLE Employee
(Eid INT PRIMARY KEY IDENTITY,
FirstName varchar(50),
LastName varchar(50),
Telephone int,
Email varchar(50),
Salary int,
Experience int,
WeeklyHours int)

CREATE TABLE CustomerOrder
(Oid INT PRIMARY KEY IDENTITY,
Price float check(Price>0) default(1.0),
Status varchar(50) default('processing'),
Cid INT FOREIGN KEY REFERENCES Customer(Cid) on delete cascade,
Tid INT FOREIGN KEY REFERENCES TimeDate(Tid) on delete cascade,
Eid INT FOREIGN KEY REFERENCES Employee(Eid) on delete cascade,
Did INT FOREIGN KEY REFERENCES Discount(Did) on delete cascade)

CREATE TABLE PacketOrder
(Pid INT FOREIGN KEY REFERENCES TeaPacket(Pid) on delete cascade,
Oid INT FOREIGN KEY REFERENCES CustomerOrder(Oid) on delete cascade,
POid int primary key identity(1,1))

CREATE TABLE Payment
(Paid INT FOREIGN KEY REFERENCES CustomerOrder(Oid) on delete cascade,
Status varchar(50),
Value float check(Value >= 0),
Bank varchar(50),
CONSTRAINT pk_OrderPayment PRIMARY KEY(Paid))
