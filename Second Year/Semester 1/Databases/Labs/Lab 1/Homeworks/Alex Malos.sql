create table MainDirector
	(MDid int PRIMARY KEY identity(1000,1),
	DirName varchar(200) not null,
	Age tinyint,
	MainGenre varchar(100))


create table Composer
	(CompId int PRIMARY KEY identity(2000, 1),
	CompName varchar(200) not null,
	Age tinyint,
	MainGenre varchar(100))

create table Language
	(Lid tinyint Primary key identity(1, 1),
	LName varchar(200) not null)

create table Store
	(Sid tinyint Primary key identity(100, 1),
	Addres varchar(200))

create table Employee
	(Eid int Primary key identity(10000, 1),
	Position varchar(200),
	EName varchar(200),
	EAddress varchar(200),
	EPhone char(12),
	EMail varchar(100),
	Salary tinyint,
	Sid tinyint references Store(Sid))

create table Company
	(Cid int Primary key identity(8000, 1),
	CName varchar(200),
	Loc varchar(100),
	NrEmployees tinyint not null)

create table Console
	(ConsId int Primary key identity(8500, 1),
	ConName varchar(200),
	ReleaseDate char(10),
	Price tinyint,
	ComId int references Company(Cid))

create table MinSpecs
	(SpID int Primary key identity(8600, 1),
	Cons int references Console(ConsId) default 0,
	CPU varchar(20),
	GPU varchar(20),
	Storage tinyint,
	Ram tinyint)

create table videoGame
	(VGid int Primary key identity(6000,1),
	VGName varchar(200),
	Genre varchar(200),
	Lid tinyint references Language(Lid),
	Cid int references company(Cid),
	MDid int references MainDirector(MDid),
	StoreId tinyint references Store(Sid),
	SpID int references MinSpecs(SpID))

create table composedFor
	(cid int references Composer(CompId),
	vid int references videoGame(VGid),
	primary key (cid,vid))

create table Client
	(clientId int primary key identity(11000, 1),
	name varchar(200),
	address varchar(200),
	age int,
	Sid tinyint references Store(Sid),
	VGId int references videoGame(VGid) Unique)