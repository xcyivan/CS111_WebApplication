package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URL;
import java.net.URI;
import java.io.InputStream;


public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        String query = ((String)request.getParameter("q")).trim();
        URL googleURL = new URL ("http://google.com/complete/search?output=toolbar&q=" + query);
        HttpURLConnection googleRequest = (HttpURLConnection) googleURL.openConnection();
        
        if (googleRequest.getResponseCode() ==HttpURLConnection.HTTP_OK)
        {
            response.setContentType("text/xml");
            
            InputStream input_stream = googleRequest.getInputStream();
            
            byte[] buffer = new byte[1024];
            int bytes_read;
            while ((bytes_read = input_stream.read(buffer)) != -1)
            {
                response.getOutputStream().write(buffer, 0, bytes_read);
            }

            response.getOutputStream().flush();
            response.getOutputStream().close();
         
        }
         
    }
}
