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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;



public class XMLParser{

	static DocumentBuilder builder;

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

    private Item returnItem(Element root){
    	Item item = new Item(root.getAttribute("ItemID"));
    	NodeList list = root.getChildNodes();
    	for(int i=0; i<list.getLength(); i++){
    		Node subnode = list.item(i);
            String nodeName = subnode.getNodeName();
            if(nodeName.equals("#text")) continue;
    		switch(nodeName){
    			case "Name":
    				item.name = getElementText((Element)subnode);
    			break;
    			case "Category":
    				item.categories.add(getElementText((Element)subnode));
    			break;
    			case "Currently":
    				item.currently = getElementText((Element)subnode);
    			break;
    			case "Buy_Price":
                    item.buyPrice = getElementText((Element)subnode);
    			break;
    			case "First_Bid":
    				item.firstBid = getElementText((Element)subnode);
    			break;
    			case "Number_of_Bids":
    				item.numOfBids = getElementText((Element)subnode);
    			break;
    			case "Bids":
    				Element[] bids_array = getElementsByTagNameNR((Element)subnode, "Bid");
    				for(int j=0; j<bids_array.length; j++){
    					Element bid = bids_array[j];
    					Element bidder = getElementByTagNameNR(bid, "Bidder");
    					item.bids.add(new Bid(new User(bidder.getAttribute("UserID")
    												  ,bidder.getAttribute("Rating")
    												  ,getElementTextByTagNameNR(bidder, "Location")
    												  ,getElementTextByTagNameNR(bidder, "Country"))
    										 ,getElementTextByTagNameNR(bid, "Time")
    										 ,getElementTextByTagNameNR(bid, "Amount")));
    				}
    			break;
    			case "Location":
    				item.location = getElementText((Element)subnode);
    				item.latitude = ((Element)subnode).getAttribute("Latitude");
    				item.longitude = ((Element)subnode).getAttribute("Longitude");
    			break;
    			case "Country":
    				item.country = getElementText((Element)subnode);
    			break;
    			case "Started":
    				item.started = getElementText((Element)subnode);
    			break;
    			case "Ends":
    				item.ends = getElementText((Element)subnode);
    			break;
    			case "Seller":
    				item.seller = new User(((Element)subnode).getAttribute("UserID"), ((Element)subnode).getAttribute("Rating"));
    			break;
    			case "Description":
    				item.description = getElementText((Element)subnode);
    			break;
    			default:
    				// System.out.println("Warning! Unknown node type!");
    			break;
    		}
    	}

        item.buyPrice = (item.buyPrice == null || item.buyPrice.length()==0) ? "Not Available" : item.buyPrice;
        item.latitude = (item.latitude == null || item.latitude.length() == 0) ? "Not Available" : item.latitude;
        item.longitude = (item.longitude == null || item.longitude.length() == 0) ? "Not Available" : item.longitude;
        item.description = (item.description == null || item.description.length()==0) ? "Not Available" : item.description;
        Collections.sort(item.bids, Collections.reverseOrder(new MyComparator()));
    	return item;
    }

	public Item doParse(String xmlString){
        System.out.println("begin parsing XML String");
		Document doc = null;
		try {
		    doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
		    Element root = doc.getDocumentElement();
		    return returnItem(root);
		}
		catch (IOException e) {
		    e.printStackTrace();
		    System.exit(3);
		}
		catch (SAXException e) {
		    System.out.println("  (not supposed to happen with supplied XML sourcs)");
		    e.printStackTrace();
		    System.exit(3);
		}
		return null;
	}

	static{
		/* Initialize parser. */
		try {
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    factory.setValidating(false);
		    factory.setIgnoringElementContentWhitespace(false);      
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
	}
}