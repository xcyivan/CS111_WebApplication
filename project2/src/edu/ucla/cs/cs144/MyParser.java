/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|||*|||";
    static DocumentBuilder builder;
    static boolean APPEND = true;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Generate the item_cat(ItemID, Category) file from a given root Node
     * A corresponding file is generated in /src directory
     * within the file, the column is separated by ','
     */
    static private void generateItemCatFile(Element root){
    	Element[] itemArr = getElementsByTagNameNR(root, "Item");
    	try{
	    	FileWriter writer = new FileWriter("item_cat.dat", APPEND);
	    	for(int i=0; i<itemArr.length; i++){
	    		Element item = itemArr[i];
	    		String itemID = item.getAttribute("ItemID");
		    	Element[] catArr = getElementsByTagNameNR(item, "Category");
		    	
		    	for(int j=0; j<catArr.length; j++){
		    		String category = getElementText(catArr[j]);
		    		String content = itemID + columnSeparator + category+'\n';
		    		writer.write(content);
		    	}
	    	}
	    	writer.close();
    	}
    	catch(IOException exception){
    		System.out.println("file couldn't be created");
    		exception.printStackTrace();
    	}
    	System.out.println("item_cat file has been successfully generated!");
    }
    
    /* Generate the item(ItemID, Name, Currently, FirstBid, Location, 
     * LocationLatitude, LocationLongitude, Country, Description) File 
     * @param-root input is the root
     * a corresponding file will be generating at "/src"
     * columns are separated by ','
     */
    static void generateItemFile(Element root){
    	Element[] itemArr = getElementsByTagNameNR(root, "Item");
    	try{
    		FileWriter writer = new FileWriter("item.dat", APPEND);
	    	for(int i=0; i<itemArr.length; i++){
	    		Element item = itemArr[i];
	    		String itemID = item.getAttribute("ItemID");
	    		String name = getElementTextByTagNameNR(item, "Name");
	    		String currently = strip(getElementTextByTagNameNR(item, "Currently"));
	    		String firstBid = strip(getElementTextByTagNameNR(item, "First_Bid"));
	    		String country = getElementTextByTagNameNR(item, "Country");
	    		String description = getElementTextByTagNameNR(item, "Description");
	    		Element locationNode = getElementByTagNameNR(item, "Location");
	    		String location = getElementText(locationNode);
	    		String locationLatitude = locationNode.getAttribute("Latitude");
	    		String locationLongitude = locationNode.getAttribute("Longitude");
	    		String content = itemID+columnSeparator+name+columnSeparator+currently+columnSeparator+firstBid+columnSeparator+
	    						 location+columnSeparator+locationLatitude+columnSeparator+locationLongitude+columnSeparator+
	    						 country+columnSeparator+description+'\n';
	    		writer.write(content);
	    	}
	    	writer.close();
    	}
    	catch(IOException exception){
    		System.out.println("file couldn't be created");
    		exception.printStackTrace();
    	}
    	System.out.println("item file has been successfully generated!");
    }
    
    /* Generate sells_item(ItemID, SellerID, StartDate, EndDate, BuyPrice, NumBids) file
     * and seller(SellerID, Rating) file
     * input is the root Element
     */
    static void generateSellFiles(Element root){
    	Element[] itemArr = getElementsByTagNameNR(root, "Item");
    	try{
    		FileWriter writer1 = new FileWriter("seller.dat", APPEND);
    		FileWriter writer2 = new FileWriter("sells_item.dat", APPEND);
    		
	    	for(int i=0; i<itemArr.length; i++){
	    		Element item = itemArr[i];
	    		String itemID = item.getAttribute("ItemID");
	    		Element seller = getElementByTagNameNR(item, "Seller");
	    		String rating = seller.getAttribute("Rating");
	    		String sellerID = seller.getAttribute("UserID");
	    		String rawStarted = getElementTextByTagNameNR(item,"Started");
	    		String rawEnds = getElementTextByTagNameNR(item,"Ends");
	    		String rawBuyPrice = getElementTextByTagNameNR(item,"Buy_Price");
	    		String buyPrice = rawBuyPrice==null ? "" : strip(rawBuyPrice);
	    		String numBids = getElementTextByTagNameNR(item,"Number_of_Bids");
	    		
	    		String content1 = sellerID+columnSeparator+rating+'\n';
	    		writer1.write(content1);
	    		
	    		//transform datetime from MMM-dd-yy HH:mm:ss to yyyy-MM-dd HH:mm:ss
	    		SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
	    		
	    		try{
	    			Date parsedStart = format.parse(rawStarted);
	    			Date parsedEnds = format.parse(rawEnds);
	    			format.applyPattern("yyyy-MM-dd HH:mm:ss");
	    			String started = format.format(parsedStart);
	    			String ends = format.format(parsedEnds);

	    			String content2 = itemID+columnSeparator+sellerID+columnSeparator+started+columnSeparator+
	    							  ends+columnSeparator+buyPrice+columnSeparator+numBids+'\n';
	    			writer2.write(content2);
	    		}
	    		catch(ParseException pe){
	    			System.out.println("ERROR: Cannot parse \"" + rawStarted +
	    								"\"" +rawEnds + "\"");
	    		}
	    	}
	    	writer1.close();
	    	writer2.close();
    	}
    	catch(IOException exception){
    		System.out.println("file couldn't be created");
    		exception.printStackTrace();
    	}

        System.out.println("sells_item file has been successfully generated!");
        System.out.println("seller file has been successfully generated!");
    	
    }
    
    /* Generate bids_item(ItemID, BidderID, Time, Amount) File
     * and bidder(BidderID, Rating, Location, Country) File
     * at "src/" separting by ','
     */
    static void generateBidFiles(Element root){
    	Element[] itemArr = getElementsByTagNameNR(root, "Item");
    	try{
    		FileWriter writer1 = new FileWriter("bidder.dat", APPEND);
    		FileWriter writer2 = new FileWriter("bids_item.dat", APPEND);
    		
	    	for(int i=0; i<itemArr.length; i++){
	    		Element item = itemArr[i];
	    		String itemID = item.getAttribute("ItemID");
	    		Element bids = getElementByTagNameNR(item,"Bids");
	    		Element[] bidArr = getElementsByTagNameNR(bids,"Bid");
	    		for(int j=0; j<bidArr.length; j++){
	    			Element bid = bidArr[j];
	    			Element bidder = getElementByTagNameNR(bid, "Bidder");
	    			String bidderID = bidder.getAttribute("UserID");
	    			String rating = bidder.getAttribute("Rating");
                    String location = getElementTextByTagNameNR(bidder,"Location");
                    String country = getElementTextByTagNameNR(bidder,"Country");
	    			String rawTime = getElementTextByTagNameNR(bid, "Time");
	    			String amount = strip(getElementTextByTagNameNR(bid,"Amount"));
	    			
	    			String content1 = bidderID+columnSeparator+rating+columnSeparator+location+columnSeparator+country+"\n";
	    			writer1.write(content1);
	    			
	    			try{
	    				//transform datetime from MMM-dd-yy HH:mm:ss to yyyy-MM-dd HH:mm:ss
	    	    		SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		    			Date parsedTime = format.parse(rawTime);
		    			format.applyPattern("yyyy-MM-dd HH:mm:ss");
		    			String time = format.format(parsedTime);
		    			
		    			String content2 = itemID+columnSeparator+bidderID+columnSeparator+time+columnSeparator+amount+"\n";
		    			writer2.write(content2);

		    		}
		    		catch(ParseException pe){
		    			System.out.println("ERROR: Cannot parse \"" + rawTime + "\"");
		    		}
	    		}
	    	}
	    	writer1.close();
	    	writer2.close();
	    	
    	}
    	catch(IOException exception){
    		System.out.println("file couldn't be created");
    		exception.printStackTrace();
    	}

        System.out.println("bid_item file has been successfully generated!");
        System.out.println("bidder file has been successfully generated!");
    	
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) throws FileNotFoundException {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element root = doc.getDocumentElement();
        generateItemCatFile(root);
        generateItemFile(root);
        generateSellFiles(root);
        generateBidFiles(root);

        System.out.println("Successfully processed - " + xmlFile);
        
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
