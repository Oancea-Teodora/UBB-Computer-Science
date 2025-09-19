CREATE TABLE Users (
    uid char(10) PRIMARY KEY,
    username varchar(20) UNIQUE,
    email varchar(50) UNIQUE
);

CREATE TABLE Director (
    did char(10) PRIMARY KEY,
    name varchar(70),
    birth_date date
);

CREATE TABLE Movie (
    mid char(10) PRIMARY KEY,
    title varchar(70),
    genre char(10),
    year_of_release SMALLINT,
    description varchar(2000),
    rating TINYINT,
    runtime INT,
    box_office DECIMAL(12, 2),
    did char(10),
    FOREIGN KEY (did) REFERENCES Director(did)
);

CREATE TABLE Show (
    sid char(10) PRIMARY KEY,
    title varchar(70),
    genre char(10),
    year_of_release SMALLINT,
    seasons TINYINT,
    description varchar(2000),
    rating TINYINT
);

CREATE TABLE Season (
    ssid char(10) PRIMARY KEY,
    number TINYINT,
    year_of_release SMALLINT,
    episodes INT,
    rating TINYINT,
    sid char(10),
    FOREIGN KEY (sid) REFERENCES Show(sid)
);

CREATE TABLE Episode (
    eid char(10) PRIMARY KEY,
    title varchar(70),
    date_of_release DATE,
    runtime INT,
    description varchar(max),
    rating TINYINT,
    ssid char(10),
    FOREIGN KEY (ssid) REFERENCES Season(ssid)
);

CREATE TABLE Actor (
    aid char(10) PRIMARY KEY,
    name varchar(70),
    birth_date date
);

CREATE TABLE User_Movie (
    uid char(10),
    mid char(10),
    status varchar(20) CHECK (status IN ('watchlist', 'watching', 'watched', 'abandoned')),
    PRIMARY KEY (uid, mid),
    FOREIGN KEY (uid) REFERENCES Users(uid),
    FOREIGN KEY (mid) REFERENCES Movie(mid)
);

CREATE TABLE User_Show (
    uid char(10),
    sid char(10),
    status varchar(20) CHECK (status IN ('watchlist', 'watching', 'watched', 'abandoned')),
    PRIMARY KEY (uid, sid),
    FOREIGN KEY (uid) REFERENCES Users(uid),
    FOREIGN KEY (sid) REFERENCES Show(sid)
);

CREATE TABLE Actor_Movie (
    aid char(10),
    mid char(10),
    PRIMARY KEY (aid, mid),
    FOREIGN KEY (aid) REFERENCES Actor(aid),
    FOREIGN KEY (mid) REFERENCES Movie(mid)
);

CREATE TABLE Actor_Show (
    aid char(10),
    sid char(10),
    PRIMARY KEY (aid, sid),
    FOREIGN KEY (aid) REFERENCES Actor(aid),
    FOREIGN KEY (sid) REFERENCES Show(sid)
);