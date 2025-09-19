use OnlineAuction
go

-- SELLERS
CREATE TABLE Sellers
(sid INT PRIMARY KEY IDENTITY,
first_name varchar(15) NOT NULL,
last_name varchar(15) NOT NULL,
username varchar(15) DEFAULT 'anonymous',
phone_number varchar(20) NOT NULL,
seller_address varchar(50) NOT NULL,
balance money DEFAULT 0)

-- BIDDERS
CREATE TABLE Bidders
(bid INT PRIMARY KEY IDENTITY,
first_name varchar(15) NOT NULL,
last_name varchar(15) NOT NULL,
username varchar(15) DEFAULT 'anonymous',
email varchar(30) NOT NULL,
phone_number varchar(20) NOT NULL,
balance money DEFAULT 0)

-- AUCTIONS
CREATE TABLE Auctions
(aid INT PRIMARY KEY IDENTITY,
start_time date,
end_time date,
current_bid money DEFAULT 0,
auction_status INT CHECK (auction_status=0 OR auction_status=1),
winning_bid money)

-- BID
CREATE TABLE BidOnAuction
(bid INT FOREIGN KEY REFERENCES Bidders(bid),
aid INT FOREIGN KEY REFERENCES Auctions(aid),
CONSTRAINT pk_BidOnAuction PRIMARY KEY(bid, aid),
bid_amount money NOT NULL,
bid_time date NOT NULL)

-- ITEMS
CREATE TABLE Items
(iid INT FOREIGN KEY REFERENCES Auctions(aid),
CONSTRAINT pk_AuctionsItems PRIMARY KEY(iid),
title varchar(50) NOT NULL,
item_description varchar(300),
starting_price money DEFAULT 1,
item_photo image,
sid INT FOREIGN KEY REFERENCES Sellers(sid))

-- CATEGORIES
CREATE TABLE Categories
(cid INT PRIMARY KEY IDENTITY,
category_name varchar(20) NOT NULL,
iid INT FOREIGN KEY REFERENCES Items(iid))

-- TAGS
CREATE TABLE Tags
(tid INT PRIMARY KEY IDENTITY,
tag_name varchar(20) NOT NULL,
iid INT FOREIGN KEY REFERENCES Items(iid))

-- PAYMENTS
CREATE TABLE Payments
(pid INT FOREIGN KEY REFERENCES Auctions(aid),
CONSTRAINT pk_AuctionPayments PRIMARY KEY(pid),
amount_paid money,
payment_date date,
payment_method varchar(50))

-- WINNERS
CREATE TABLE Winners
(wid INT FOREIGN KEY REFERENCES Auctions(aid),
CONSTRAINT pk_AuctionsWinners PRIMARY KEY(wid),
first_name varchar(15),
last_name varchar(15),
email varchar(30),
phone_number varchar(20))

-- SOLDITEMS
CREATE TABLE SoldItems
(siid INT FOREIGN KEY REFERENCES Auctions(aid),
CONSTRAINT pk_AuctionsSoldItems PRIMARY KEY(siid),
item_title varchar(50),
item_photo image,
starting_price money,
winning_bid money)