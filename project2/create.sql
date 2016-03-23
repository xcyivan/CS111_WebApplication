CREATE TABLE IF NOT EXISTS item_cat(
	ItemID INTEGER NOT NULL, 
	Category VARCHAR(100) NOT NULL);


CREATE TABLE IF NOT EXISTS item(
	ItemID INTEGER NOT NULL, 
	Name VARCHAR(100) NOT NULL, 
	Currently DOUBLE(20,2) NOT NULL, 
	FirstBid DOUBLE(20,2) NOT NULL, 
	Location VARCHAR(100) NOT NULL, 
	Latitude DECIMAL(9,6) DEFAULT NULL, 
	Longitude DECIMAL(9,6) DEFAULT NULL, 
	Country VARCHAR(80) NOT NULL, 
	Description VARCHAR(4000), 
	PRIMARY KEY(ItemID));


CREATE TABLE IF NOT EXISTS sells_item(
	ItemID INTEGER NOT NULL, 
	UserID VARCHAR(100) NOT NULL, 
	StartDate DATETIME NOT NULL, 
	EndDate DATETIME NOT NULL, 
	BuyPrice DOUBLE(20,2), 
	NumBids INTEGER NOT NULL, 
	PRIMARY KEY(ItemID, UserID));


CREATE TABLE IF NOT EXISTS seller(
	UserID VARCHAR(100) NOT NULL, 
	Rating INTEGER NOT NULL, 
	PRIMARY KEY(UserID));


CREATE TABLE IF NOT EXISTS bids_item(
	ItemID INTEGER NOT NULL, 
	UserID VARCHAR(100) NOT NULL, 
	Time DATETIME NOT NULL, 
	Amount DOUBLE(20,2) NOT NULL, 
	PRIMARY KEY(ItemID, UserID, Time));


CREATE TABLE IF NOT EXISTS bidder(
	UserID VARCHAR(100) NOT NULL, 
	Rating INTEGER NOT NULL,
	Location VARCHAR(100) DEFAULT NULL,
	Country VARCHAR(100) DEFAULT NULL, 
	PRIMARY KEY(UserID));
