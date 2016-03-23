<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
    <title>Keyword Search Result</title>
    <style>
            body{
                 background-color: #66cccc;
            }
            h1 {
                padding-top: 50px;
                color: #330099;
                text-align: center;
                padding-bottom: 10px;
            }
            div{
                
                color:#333366;
                text-align: center;

            }
            table{
                 border-collapse: collapse;
                color:#333366;
            }
            table, td, th {
                 border: 1px solid black;
            }

            th,td {
                text-align: left;
                padding: 8px;
            }
             tr:nth-child(even){background-color: #99D4E9}
              tr:nth-child(odd){background-color: #99E9E9}

           
            #t{
                margin-left: 100px;
                margin-right: 100px;
                margin-bottom: 10px;
            }

            
    </style>





    <script type="text/javascript" src="autoSuggestControl.js"></script>
    <script type="text/javascript" src="suggestionProvider.js"></script>
    <link rel="stylesheet" type="text/css" href="autoSuggest.css" />
    <script type="text/javascript">
        window.onload = function(){
            var oTextbox = new AutoSuggestControl(document.getElementById("qtxt2"), new SuggestionProvider());
        }
    </script>
</head>

<body>
    <h1>Keyword Search Result</h1>
    <form method="get" action="./search">
    <div>
        Keyword: <input type="text" name="q" id="qtxt2"><br>
        <input type="hidden" name="numResultsToSkip" value = "0" readonly><br>
        <input type="hidden" name="numResultsToReturn" value = "20" readonly>
        <input type="submit" value="Search"><br>
    </div>
    
    <div id="t">
   
    <table border="1" width = "100%" color="#330099">
	

		<tr>
			<td>ItemId</td><td>Name</td>
		</tr>
    <%
        int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        String query = (String)request.getParameter("q");
        SearchResult[] results = (SearchResult[])request.getAttribute("results");

        if (query==""||results.length==0)
            out.println("<br>No results found!<br>");
       
        out.println("<br>");

        int back = 20;
        int forward = 20;

        if (numResultsToSkip < 20)
            back = 0;
        if (results.length < 20)
            forward = 0;

        %>

        
        
        <a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numResultsToSkip-back %>&numResultsToReturn=<%= numResultsToReturn %>">Previous Page</a><br>
    
        <a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numResultsToSkip+forward %>&numResultsToReturn=<%= numResultsToReturn %>">Next Page</a><br>


            <%
    	
		for(int i=0; i<results.length; i++){
			out.println("<tr>");
			out.println("<td><a href=\"./item?id="+results[i].getItemId()+"\">"+results[i].getItemId()+"</a></td>");
            out.println("<td>"+results[i].getName()+"</td>");
			out.println("</tr>");
		}
    %>


</table> 

</div>

  

</body>

</html>