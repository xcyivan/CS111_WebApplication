package edu.ucla.cs.cs144;

public class User{
	protected String userID;
	protected String rating;
	protected String location;
	protected String country;

	public User(){
		userID = rating = location = country = "";
	}

	public User(String userID, String rating){
		this.userID = userID;
		this.rating = rating;
		location = "";
		country = "";
	}

	public User(String userID, String rating, String location, String country){
		this.userID = userID;
		this.rating = rating;
		this.location = location;
		this.country = country;
	}

	public String toString(){
		String returned = userID + " " + rating;
		if(location.length()!=0) returned = returned + " " + location;
		if(country.length()!=0) returned = returned + " " + country;
		return returned;
	}

	public String getID(){
		return userID;
	}

	public String getRating(){
		return rating;
	}

	public String getLocation(){
		return location;
	}

	public String getCountry(){
		return country;
	}
}