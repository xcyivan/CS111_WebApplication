Part B:
relation: item_cat(ItemID, Catagory)
	keys: none
	Other NFD: none
	BCNF: yes	
	4NF: yes
relation: tem(ItemID, Name, Currently, FirstBid, Location, Latitude, Longitude, Country, Description)
	keys: ItemID
	other NFD: none
	BCNR: yes	
	4NF: yes
relation: sells_item(ItemID, SellerID, StartDate, EndDate, BuyPrice， NumBids)
	keys: ItemID, SellerID
	other NFD: none
	BCNR: yes	
	4NR: yes
relation: Seller(SellerID, Rating)
	keys: SellerID
	other NFD: none
	BCNR: yes
	4NR: yes
relation: bids_item(ItemID, BidderID, Time, Amount)
	keys: ItemID, BidderID, Time
	other NFD: BidderID, Time -> ItemID, Amount
	BCNR: yes
	4NR: yes
relation: Bidder(BidderID, Rating)
	keys: UserID
	other NFD: none
	BCNR: yes
	4NF: yes

//-------------------------------------------------------------------------------------------