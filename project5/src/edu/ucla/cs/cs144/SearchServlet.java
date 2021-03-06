package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String q = request.getParameter("q");
        int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        SearchResult[] results = AuctionSearchClient.basicSearch(q, numResultsToSkip, numResultsToReturn);

        request.setAttribute("q",q);
        request.setAttribute("skip",numResultsToSkip);
        request.setAttribute("return",numResultsToReturn);
        request.setAttribute("results",results);

        request.getRequestDispatcher("keywordSearchResult.jsp").forward(request, response);
    }
}
