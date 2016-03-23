CREATE TABLE IF NOT EXISTS geom(ItemID INTEGER NOT NULL, location GEOMETRY NOT NULL, PRIMARY KEY(itemID)) ENGINE = MyISAM;

CREATE SPATIAL INDEX sp_index ON geom (location);

INSERT INTO geom(ItemID, location) SELECT ItemID, Point(Latitude, Longitude) FROM item WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL;