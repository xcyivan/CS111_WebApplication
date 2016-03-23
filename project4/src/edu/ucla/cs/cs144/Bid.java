package edu.ucla.cs.cs144;

public class Bid{
	protected User bidder;
	protected String time;
	protected String amount;

	public Bid(User bidder, String time, String amount){
		this.bidder = bidder;
		this.time = time;
		this.amount = amount;
	}

	public String toString(){
		return bidder.toString() + " " + time + " " + amount;
	}

	public String toHTTP(){
		String returned = "<td>"
						+ "BidderID: " + bidder.getID() + "<br>"
						+ "BidderRating: " + bidder.getRating() + "<br>"
						+ "BidderLocation: " + bidder.getLocation() + "<br>"
						+ "BidderCountry: " + bidder.getCountry() + "<br>"
						+ "Time: "  + time + "<br>"
						+ "Amount: " + amount
						+ "</td>";
		return returned;
	}
}