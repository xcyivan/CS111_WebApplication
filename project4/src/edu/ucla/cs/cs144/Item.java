package edu.ucla.cs.cs144;

import java.util.ArrayList;


public class Item{
	protected String itemID;
	protected String name;
	protected ArrayList<String> categories;
	protected String currently;
	protected String buyPrice;
	protected String firstBid;
	protected String numOfBids;
	protected ArrayList<Bid> bids;
	protected String location;
	protected String latitude;
	protected String longitude;
	protected String country;
	protected String started;
	protected String ends;
	protected User seller;
	protected String description;

	public Item(String id){
		itemID = id;
		categories = new ArrayList<String> ();
		bids = new ArrayList<Bid> ();
		name = currently = buyPrice = firstBid = numOfBids = location = 
		latitude = longitude = country = started = ends = description = "";
	}

	public String getID(){
		return itemID;
	}

	public String getName(){
		return name;
	}

	public String[] getCategories(){
		return categories.toArray(new String[categories.size()]);
	}

	public String getCurrently(){
		return currently;
	}

	public String getBuyPrice(){
		return buyPrice;
	}

	public String getFirstBid(){
		return firstBid;
	}

	public String getNumOfBids(){
		return numOfBids;
	}

	public Bid[] getBids(){
		return bids.toArray(new Bid[bids.size()]);
	}

	public String getLocation(){
		return location;
	}

	public String getLatitude(){
		return latitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public String getCountry(){
		return country;
	}

	public String getStarted(){
		return started;
	}

	public String getEnds(){
		return ends;
	}

	public User getSeller(){
		return seller;
	}

	public String getDescription(){
		return description;
	}
}