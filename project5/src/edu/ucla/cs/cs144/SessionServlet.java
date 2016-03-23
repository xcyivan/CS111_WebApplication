package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;


public class SessionServlet extends HttpServlet implements Servlet {
       
    public SessionServlet() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String flag = request.getParameter("flag");
        PrintWriter out = response.getWriter();
        if(session.getAttribute("itemMap")==null){
          out.println("itemMap is null");
        }
        else{
          if(flag.equals("buy")){
              try{
              String id = request.getParameter("id");
              String name = (((HashMap<String, String[]>)session.getAttribute("itemMap")).get(request.getParameter("id")))[0];
              String price = (((HashMap<String, String[]>)session.getAttribute("itemMap")).get(request.getParameter("id")))[1];
              request.setAttribute("id",id);
              request.setAttribute("name",name);
              request.setAttribute("price",price);
              request.getRequestDispatcher("/payment.jsp").forward(request, response);
              }
              catch (Exception e){
                e.printStackTrace(out);
              }
          }
          if(flag.equals("confirm")){
              String id = request.getParameter("id");
              String name = (((HashMap<String, String[]>)session.getAttribute("itemMap")).get(request.getParameter("id")))[0];
              String price = (((HashMap<String, String[]>)session.getAttribute("itemMap")).get(request.getParameter("id")))[1];
              String number = request.getParameter("number");
              //credit card number validation
              

              request.setAttribute("id",id);
              request.setAttribute("name",name);
              request.setAttribute("price",price);
              request.setAttribute("number",number);
              request.getRequestDispatcher("/confirm.jsp").forward(request, response);
          }
        }
    }

  public void doPost(HttpServletRequest request,HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}
