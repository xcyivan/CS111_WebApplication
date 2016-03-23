CREATE TABLE Actors (Name VARCHAR(40), Movie VARCHAR(80), Year INTEGER, Role VARCHAR(40));
-- create a table named actors 

LOAD DATA LOCAL INFILE '~/data/actors.csv' INTO TABLE Actors
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
-- load actors.csv file into the Actors table 

SELECT DISTINCT Name 
FROM Actors
WHERE Movie = 'Die Another Day';
-- query that answers "give me the names of all the actors in the movie 'Die Another Day'"

DROP TABLE Actors;
-- drop the Actors table from MySQL 