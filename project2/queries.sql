    -- Find the number of users in the database.
    -- 13422
    SELECT COUNT(T.UserID) 
    FROM
    (SELECT UserID
    FROM seller
    UNION
    SELECT UserID
    FROM bidder) AS T;

    -- Find the number of items in "New York", (i.e., items whose location is exactly the string "New York"). Pay special attention to case sensitivity. You should match the items in "New York" but not in "new york".
    -- 103
    SELECT COUNT(DISTINCT ItemID)
    FROM item
    WHERE BINARY Location = 'New York';

    -- Find the number of auctions belonging to exactly four categories.
    -- 8365
    SELECT COUNT(*)
    FROM 
    (SELECT ItemID 
    FROM item_cat 
    GROUP BY ItemID 
    HAVING COUNT(Category)=4
    ) AS temp;

    -- Find the ID(s) of current (unsold) auction(s) with the highest bid. Remember that the data was captured at the point in time December 20th, 2001, one second after midnight, so you can use this time point to decide which auction(s) are current. Pay special attention to the current auctions without any bid.
    -- 1046740686
    SELECT sells_item.ItemID
    FROM sells_item
    INNER JOIN bids_item
    ON bids_item.ItemID = sells_item.ItemID
    WHERE EndDate > '2001-12-20 00:00:01' 
    AND Amount = (SELECT MAX(Amount) FROM bids_item);
    
    -- Find the number of sellers whose rating is higher than 1000.
    -- 3130
    SELECT COUNT(DISTINCT UserID)
    FROM seller
    WHERE Rating > 1000;

    -- Find the number of users who are both sellers and bidders.
    -- 6717
    SELECT COUNT(DISTINCT UserID)
    FROM seller
    WHERE UserID IN (SELECT UserID FROM bidder);

    -- Find the number of categories that include at least one item with a bid of more than $100.
    -- 150
    SELECT COUNT(DISTINCT item_cat.Category)
    FROM item_cat
    INNER JOIN bids_item
    ON item_cat.ItemID=bids_item.ItemID
    WHERE Amount > 100
    AND bids_item.ItemID IN
    (SELECT bids_item.ItemID FROM bids_item INNER JOIN item ON bids_item.ItemID=item.ItemID);