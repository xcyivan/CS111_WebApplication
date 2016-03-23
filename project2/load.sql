LOAD DATA LOCAL INFILE 'unqitem_cat.dat' 
INTO TABLE item_cat 
FIELDS TERMINATED BY '|||*|||';

LOAD DATA LOCAL INFILE 'unqitem.dat' 
INTO TABLE item 
FIELDS TERMINATED BY '|||*|||'
(ItemID, Name, Currently, FirstBid, Location, @vLatitude, @vLongitude, Country, Description)
SET
Latitude = nullif(@vLatitude,''),
Longitude = nullif(@vLongitude,'')
;

LOAD DATA LOCAL INFILE 'unqsells_item.dat' 
INTO TABLE sells_item 
FIELDS TERMINATED BY '|||*|||'
(ItemID, UserID, StartDate, EndDate, @vBuyPrice, NumBids)
SET
BuyPrice = nullif(@vBuyPrice,'')
;

LOAD DATA LOCAL INFILE 'unqseller.dat' 
INTO TABLE seller 
FIELDS TERMINATED BY '|||*|||';

LOAD DATA LOCAL INFILE 'unqbids_item.dat' 
INTO TABLE bids_item 
FIELDS TERMINATED BY '|||*|||';

LOAD DATA LOCAL INFILE 'unqbidder.dat' 
INTO TABLE bidder 
FIELDS TERMINATED BY '|||*|||'
(UserID, Rating, @vLocation, @vCountry)
SET
Location = nullif(@vLocation,''),
Country = nullif(@vCountry,'')
;
