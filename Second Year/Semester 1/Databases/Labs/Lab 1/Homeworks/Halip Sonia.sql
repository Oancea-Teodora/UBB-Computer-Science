use Bakery

create table Address(
zipcode char(6) primary key,
street_name varchar(20),
street_number int,
city varchar(20)
)

create table Bakery(
sid int primary key identity,
addr char(6) references Address(zipcode),
name varchar(20),
staff varchar(30)
)

create table Customer(
phone_number char(12) primary key,
name varchar(20),
address char(6) references Address(zipcode),
sid int references Bakery(sid)
)

create table Products(
pid int identity(1,1) primary key,
name varchar(20),
description varchar(50) default null
)

create table Orders(
oid bigint identity(100,1) primary key,
deadline date default getdate(),
customer char(12) references Customer(phone_number)
)

create table OrdProd(
oid bigint references Orders(oid),
pid int references Products(pid),
constraint or_pr primary key(oid,pid),
quantity int default 1
)

create table Supplier(
supplier_id int primary key identity,
name varchar(20),
phone_number char(12)
)

create table Ingredient(
ingr_name varchar(20) primary key,
supplier int references Supplier(supplier_id) unique,
cost_kg float
)

create table ProdIngr(
pid int references Products(pid),
ingr_name varchar(20) references Ingredient(ingr_name),
constraint pr_ingr primary key(pid,ingr_name),
quantity_kg float
)

create table Position(
name varchar(20) primary key,
salary float,
nr_staff int identity
)

create table Staff(
staff_id int primary key,
name varchar(30),
phone_number char(12),
position varchar(20) references Position(name),
bonus float default 0.0,
sid int references Bakery(sid)
)

alter table Position
drop column nr_staff





