package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.*;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {
	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are NOT public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch (String query, int numResultsToSkip, int numResultsToReturn){
		if(numResultsToSkip<0 || numResultsToReturn<0){
			return new SearchResult[0];
		}
		try{
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
			QueryParser parser = new QueryParser("content", new StandardAnalyzer());
			Query q = parser.parse(query);
			TopDocs topDocs = searcher.search(q, numResultsToSkip+numResultsToReturn);
			System.out.println("Results found: " + topDocs.totalHits);
			ScoreDoc[] hits = topDocs.scoreDocs;
			System.out.println("hits.lenght: " + hits.length);
			ArrayList<SearchResult> result = new ArrayList<SearchResult>();
			for (int i = numResultsToSkip; i < hits.length; i++) {
	            Document doc = searcher.doc(hits[i].doc);
	            result.add(new SearchResult(doc.get("itemID"), doc.get("name")));
	            // System.out.println(doc.get("itemID")
	            //                    + " " + doc.get("name")
	            //                    + " (" + hits[i].score + ")");
        	}
        	return result.toArray(new SearchResult[result.size()]);
		}
		catch(IOException ioe){
			System.out.println("can't open indexer file");
		}
		catch(ParseException pe){
			System.out.println("can't parse query");
		}
		return new SearchResult[0];
	}





	public SearchResult[] spatialSearch(String query, 
										SearchRegion region,
										int numResultsToSkip, 
										int numResultsToReturn)
	{
		if(numResultsToSkip<0 || numResultsToReturn<0){
			return new SearchResult[0];
		}
		HashMap<String, SearchResult> basicResult = new HashMap<String, SearchResult>();
		HashMap<String, SearchResult> spatialMatch = new HashMap<String, SearchResult>();
		//basic keyword search
		try{
			//searcher set up
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
			QueryParser parser = new QueryParser("content", new StandardAnalyzer());
			//basic search
			Query q = parser.parse(query);
			TopDocs topDocs = searcher.search(q, Integer.MAX_VALUE);
			// System.out.println("Results found: " + topDocs.totalHits);
			ScoreDoc[] hits = topDocs.scoreDocs;
			// System.out.println("hits.lenght: " + hits.length);
			SearchResult[] result = new SearchResult[numResultsToReturn];
			for (int i = 0; i < hits.length; i++) {
	            Document doc = searcher.doc(hits[i].doc);
	            basicResult.put(doc.get("itemID"),new SearchResult(doc.get("itemID"), doc.get("name")));
	            // System.out.println("basic found id: " + doc.get("itemID"));
        	}
		}
		catch(IOException ioe){
			System.out.println("can't open indexer file");
		}
		catch(ParseException pe){
			System.out.println("can't parse query");
		}

		System.out.println("basicSearch found in total : " + basicResult.size() + " results");

		//spatial search
		
		double lx=region.getLx();
		double ly=region.getLy();
		double rx=region.getRx();
		double ry=region.getRy();
		String polygon = "GeomFromText('Polygon(("
							+lx+" "+ly+","
							+rx+" "+ly+","
							+rx+" "+ry+","
							+lx+" "+ry+","
							+lx+" "+ly+"))')";
		Connection conn = null;
		try {
		    conn = DbManager.getConnection(true);
	        Statement s = conn.createStatement();

	        //fill itemID and category field
	        ResultSet rs = s.executeQuery("SELECT ItemID, AsTEXT(location) FROM geom WHERE MBRContains("+polygon+", location);");
	        while( rs.next() ){
	            String itemID = "" + rs.getInt("ItemID");
	            // System.out.println("spatial search found id: " + itemID);
	            if(basicResult.containsKey(itemID)){
	            	spatialMatch.put(itemID,basicResult.get(itemID));
	            	// System.out.println("double matched: " + itemID);
	            }
        	}
        	conn.close();
        }
        catch (SQLException ex) {
	    	System.out.println(ex);
		}

		//return the right size
		ArrayList<SearchResult> result = new ArrayList<SearchResult>();
		int added=0;
		for(SearchResult re : spatialMatch.values()){
			if(added<numResultsToReturn){
				result.add(new SearchResult(re.getItemId(),re.getName()));
			}
		}
		return result.toArray(new SearchResult[result.size()]);
	}

	private String returnedXML;
	private enum ElementType{
		ITEMID,
		NAME,
		CURRENTLY,
		FIRSTBID,
		LOCATION_ONLY,
		LOCATION_LATITUDE_LONGITUDE,
		LATITUDE,
		LONGITUDE,
		COUNTRY,
		DESCRIPTION,
		SELLERRATING,
		SELLERID,
		STARTDATE,
		ENDDATE,
		BUYPRICE,
		NUMBIDS,
		TIME,
		AMOUNT,
		CATEGORY,
		CLOSE
	}
    
    //escape
    private String escaped_string (String content)
    {
        if (content == null)
            return null;
        String escaped = "";
        char c;
        
        for (int i=0; i<content.length();i++)
        {
            c=content.charAt(i);
            switch(c)
            {
                case '\"':
                    escaped += "&quot;";
                    break;
                case '\'':
                    escaped += "&apos;";
                    break;
                case '&':
                    escaped += "&amp;";
                    break;
                case '<':
                    escaped += "&lt;";
                    break;
                case '>':
                    escaped += "&gt;";
                    break;
                default:
                    escaped += c;
            }
        }
        return escaped;
    }
    
    
	private void appendXML(String content, ElementType type){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        
        
		switch (type){
			case ITEMID:
				returnedXML = returnedXML.concat("<Item ItemID=\""+content+"\">\n");
				break;

			case NAME:
				returnedXML = returnedXML.concat("<Name>"+content+"</Name>\n");
				break;

			case CURRENTLY:
				returnedXML = returnedXML.concat("<Currently>$"+content+"</Currently>\n");
				break;

			case FIRSTBID:
				returnedXML = returnedXML.concat("<First_Bid>$"+content+"</First_Bid>\n");
				break;

			case LOCATION_ONLY:
				returnedXML = returnedXML.concat("<Location>"+content+"</Location>\n");
				break;

			case LOCATION_LATITUDE_LONGITUDE:
				returnedXML = returnedXML.concat(content+"</Location>\n");
				break;

			case LATITUDE:
				returnedXML = returnedXML.concat("<Location Latitude=\""+content+"\" ");
				break;

			case LONGITUDE:
				returnedXML = returnedXML.concat("Longitude=\""+content+"\">");
				break;

			case COUNTRY:
				returnedXML = returnedXML.concat("<Country>"+content+"</Country>\n");
				break;

			case DESCRIPTION:
				returnedXML = returnedXML.concat("<Description>"+content+"</Description>\n");
				break;

			case SELLERRATING:
				returnedXML = returnedXML.concat("<Seller Rating=\""+content+"\" ");
				break;

			case SELLERID:
				returnedXML = returnedXML.concat("UserID=\""+content+"\" />\n");
				break;

			case STARTDATE:
	        	try{
					Date parsedStart = format.parse(content);
					format.applyPattern("MMM-dd-yy HH:mm:ss");
					String newstart = format.format(parsedStart);
					returnedXML = returnedXML.concat("<Started>"+newstart+"</Started>\n");
				}
				catch(java.text.ParseException e){
					System.out.println("can't parse startdate");
				}
				break;

			case ENDDATE:
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	try{
					Date parsedEnd = format.parse(content);
					format.applyPattern("MMM-dd-yy HH:mm:ss");
					String newend = format.format(parsedEnd);
					returnedXML = returnedXML.concat("<Ends>"+newend+"</Ends>\n");
				}
				catch(java.text.ParseException e){
					System.out.println("can't parse enddate");
				}
				break;

			case BUYPRICE:
				returnedXML = returnedXML.concat("<Buy_Price>$"+content+"</Buy_Price>\n");
				break;

			case NUMBIDS:
				returnedXML = returnedXML.concat("<Number_of_Bids>"+content+"</Number_of_Bids>\n");
				break;

			case CATEGORY:
				returnedXML = returnedXML.concat("<Category>"+content+"</Category>\n");
				break;

			case TIME:
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	try{
					Date parsedtime = format.parse(content);
					format.applyPattern("MMM-dd-yy HH:mm:ss");
					String newtime = format.format(parsedtime);
					returnedXML = returnedXML.concat("<Time>"+newtime+"</Time>\n");
				}
				catch(java.text.ParseException e){
					System.out.println("can't parse time");
				}
				break;

			case AMOUNT:
				returnedXML = returnedXML.concat("<Amount>$"+content+"</Amount>\n");
				break;

			case CLOSE:
				returnedXML = returnedXML.concat("</Item>");
				break;

			//Bidder information are appended inline due to its complexity

			default:
				System.out.println("unknown type passed in");
				break;
		}
	}
    
   
    

	public String getXMLDataForItemId(String itemId) {
		returnedXML="";
		String query0 = "SELECT itemID, Name, Currently, FirstBid, Location, Latitude, Longitude, Country, Description FROM item WHERE itemID="+itemId+";";
		String query1 = "SELECT itemID, sells_item.UserID, Rating, StartDate, EndDate, BuyPrice, NumBids FROM sells_item JOIN seller ON sells_item.userid=seller.userid WHERE sells_item.itemID="+itemId+";";
		String query2 = "SELECT itemID, Category FROM item_cat WHERE ItemID="+itemId+";";
		String query3 = "SELECT itemID, bids_item.UserID, Rating, Location, Country, Time, Amount FROM bids_item JOIN bidder ON bids_item.UserID=bidder.UserID WHERE itemID="+itemId+";";
		Connection conn = null;
		try {
		    conn = DbManager.getConnection(true);
	        Statement s = conn.createStatement();

	        ResultSet rs0 = s.executeQuery(query0);
	        
	        while( rs0.next() ){
	            String id = "" + rs0.getInt("itemID");
	            String name = escaped_string(rs0.getString("Name"));
	            String currently = escaped_string(rs0.getString("Currently"));
	            String firstbid = escaped_string(rs0.getString("FirstBid"));
	            String location = escaped_string(rs0.getString("Location"));
	            String latitude = rs0.getString("Latitude");
	            String longitude = rs0.getString("Longitude");
	            String country = escaped_string(rs0.getString("Country"));
	            String description = escaped_string(rs0.getString("Description"));

	            appendXML(id, ElementType.ITEMID);
	            appendXML(name, ElementType.NAME);
	            appendXML(currently, ElementType.CURRENTLY);
	            appendXML(firstbid, ElementType.FIRSTBID);
	            if(latitude==null && longitude==null){
	            	appendXML(location, ElementType.LOCATION_ONLY);
	            }
	            else{
	            	appendXML(latitude, ElementType.LATITUDE);
	            	appendXML(longitude, ElementType.LONGITUDE);
	            	appendXML(location, ElementType.LOCATION_LATITUDE_LONGITUDE);
	            }
	            appendXML(country, ElementType.COUNTRY);
	            appendXML(description, ElementType.DESCRIPTION);
        	}

        	ResultSet rs1 = s.executeQuery(query1);
	        while( rs1.next() ){
	        	String sellerid = rs1.getString("UserID");
	        	String sellerraing = rs1.getString("Rating");
	        	String startdate = escaped_string(rs1.getString("StartDate"));
	        	String enddate = escaped_string(rs1.getString("EndDate"));
	        	String buyprice = escaped_string(rs1.getString("BuyPrice"));
	        	String numbids = escaped_string(rs1.getString("NumBids"));

	        	appendXML(sellerraing, ElementType.SELLERRATING);
	        	appendXML(sellerid, ElementType.SELLERID);
				appendXML(startdate, ElementType.STARTDATE);
				appendXML(enddate, ElementType.ENDDATE);
				if(buyprice!=null){
					appendXML(buyprice, ElementType.BUYPRICE);
				}
				appendXML(numbids, ElementType.NUMBIDS);
				
        	}

        	ResultSet rs2 = s.executeQuery(query2);
        	while( rs2.next() ){
        		String category = escaped_string(rs2.getString("Category"));
        		appendXML(category, ElementType.CATEGORY);
        	}

        	

        	ResultSet rs3 = s.executeQuery(query3);
        	boolean empty = true;
        	while( rs3.next() ){
        		String bidderid = rs3.getString("UserID");
        		String bidderrating = rs3.getString("Rating");
        		String location = escaped_string(rs3.getString("Location"));
        		String country = escaped_string(rs3.getString("Country"));
        		String time = escaped_string(rs3.getString("Time"));
        		String amount = escaped_string(rs3.getString("Amount"));
        		if(empty){
        			empty = false;
        			returnedXML = returnedXML.concat("<Bids>\n");
        		}

    			returnedXML = returnedXML.concat("<Bid>\n");

    			if(location!=null && country!=null){
    				returnedXML = returnedXML.concat("<Bidder Rating=\""+bidderrating+"\" UserID=\""+bidderid+"\">\n");
    				returnedXML = returnedXML.concat("<Location>"+location+"</Location>\n");
    				returnedXML = returnedXML.concat("<Country>"+country+"</Country>\n");
    				returnedXML = returnedXML.concat("</Bidder>\n");
    			}
    			else if(location!=null && country==null){
    				returnedXML = returnedXML.concat("<Bidder Rating=\""+bidderrating+"\" UserID=\""+bidderid+"\">\n");
    				returnedXML = returnedXML.concat("<Location>"+location+"</Location>\n");
    				returnedXML = returnedXML.concat("</Bidder>\n");
    			}
    			else if(location==null && country!=null){
    				returnedXML = returnedXML.concat("<Bidder Rating=\""+bidderrating+"\" UserID=\""+bidderid+"\">\n");
    				returnedXML = returnedXML.concat("<Country>"+country+"</Country>\n");
    				returnedXML = returnedXML.concat("</Bidder>\n");
    			}
    			else{
    				returnedXML = returnedXML.concat("<Bidder Rating=\""+bidderrating+"\" UserID=\""+bidderid+"\" />\n");
    			}

    			appendXML(time, ElementType.TIME);
    			appendXML(amount, ElementType.AMOUNT);

    			returnedXML = returnedXML.concat("</Bid>\n");

        	}
        	if(empty){
        		returnedXML = returnedXML.concat("<Bids />\n");
        	}
        	else{
        		returnedXML = returnedXML.concat("</Bids>\n");
        	}
        	appendXML("",ElementType.CLOSE);
        	conn.close();
        }
        catch (SQLException ex) {
	    	System.out.println(ex);
		}
		return returnedXML;
	}
	
	public String echo(String message) {
		return message;
	}

}
