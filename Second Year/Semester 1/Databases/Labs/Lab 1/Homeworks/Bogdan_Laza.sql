USE SportsFaility1
go



    CREATE TABLE Janitor (
        jid INT NOT NULL PRIMARY KEY,
        jname VARCHAR(25) NULL
    );

CREATE TABLE Court (
    cid INT NOT NULL PRIMARY KEY,      
    cname NVARCHAR(50),              
    jid INT NOT NULL,                         
    CONSTRAINT FK_jid FOREIGN KEY (jid) REFERENCES Janitor(jid)  
);

CREATE TABLE Player(
pid int NOT NULL PRIMARY KEY,
pname varchar(25),
cid int NOT NULL,
CONSTRAINT FK_cid FOREIGN KEY (cid) REFERENCES Court(cid)
);

CREATE TABLE Sport(
sname varchar(20) NOT NULL PRIMARY KEY
);


CREATE TABLE Play(
    pid int NOT NULL,
    sname varchar(20) NOT NULL,
    FOREIGN KEY (pid) REFERENCES Player(pid),
    FOREIGN KEY (sname) REFERENCES Sport(sname),
    CONSTRAINT pk_play PRIMARY KEY (pid, sname)
);


CREATE TABLE Trainer(
tname varchar(20) NOT NULL PRIMARY KEY,
sname varchar(20) NOT NULL,
CONSTRAINT FK_sname1 FOREIGN KEY(sname) REFERENCES Sport(sname)
);

CREATE TABLE Equipment(
ename varchar(20) NOT NULL PRIMARY KEY,
quantity int NULL,
CONSTRAINT FK_sname2 FOREIGN KEY(ename) REFERENCES Sport(sname) ON DELETE CASCADE
);

CREATE TABLE Bar(
bartype varchar(20) not null primary key,
seats int
);

CREATE TABLE SpaZone(
spatype varchar(20) not null primary key,
seats int
);

CREATE TABLE Membership(
mid int not null primary key,
constraint FK_mid foreign key(mid) references Player(pid),
btype varchar(20),
constraint FK_bartype foreign key(btype) references Bar(bartype),
stype varchar(20),
constraint FK_spatype foreign key(stype) references SpaZone(spatype)
);

