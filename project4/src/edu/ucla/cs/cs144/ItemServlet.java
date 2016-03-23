package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;

import java.io.PrintWriter;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        PrintWriter out = response.getWriter();
        
        String id =request.getParameter("id");
        String itemXML = AuctionSearchClient.getXMLDataForItemId(id);
        
        //out.println(id);
        if (id=="")
            out.println("Entered ID is an empty string. Please press back and enter a new ID.");
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
                e.printStackTrace();
            }
        

            request.setAttribute("item",item);
            request.getRequestDispatcher("itemResult.jsp").forward(request, response);
        }
    }
}
