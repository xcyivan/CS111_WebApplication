package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.InputStream;
import java.io.PrintWriter;

import java.util.HashMap;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        PrintWriter out = response.getWriter();
        
        String id =request.getParameter("id");
        String itemXML = AuctionSearchClient.getXMLDataForItemId(id);

        if (id==null)
        {
            out.println("Error retrieving ID.");
        }
        else if (id=="")
        {
            out.println("Entered ID is an empty string. Please press back and enter a new ID.");
        }
        else if (itemXML==null)
        {
            out.println("Can't retrieve data from oak server");
        }
        else if (itemXML=="")
        {
            out.println("No matching ID. Please press back and enter a new ID.");
        }
        else
        {
            XMLParser parser = new XMLParser();
            Item item = null;

            try{
                item = parser.doParse(itemXML);
            }
            catch(Exception e){
                e.printStackTrace(out);
            }

            if(item == null){
                out.println("item is null!");
            }
            else{
                try{
                    HttpSession session = request.getSession(true);
                    HashMap<String, String[]> itemMap;
                    if(session.isNew()) {
                        itemMap = new HashMap<String, String[]>();
                    }
                    else{
                        itemMap = (HashMap<String, String[]>)session.getAttribute("itemMap");
                        if(itemMap==null){
                            itemMap = new HashMap<String, String[]>();
                        }
                    }
                    // out.println(itemMap.toString());
                    
                    
                    if(!itemMap.containsKey(id)){
                        String[] info = new String[2];
                        info[0] = item.getName();
                        info[1] = item.getBuyPrice();
                        itemMap.put(id,info);
                        session.setAttribute("itemMap", itemMap);
                    }
                    
                    // out.println(itemMap.toString());
                }
                catch(Exception e){
                    e.printStackTrace(out);
                }

                request.setAttribute("item",item);
                request.getRequestDispatcher("itemResult.jsp").forward(request, response);
            }
        }
    }
}
