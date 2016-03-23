<%@ page import="edu.ucla.cs.cs144.Item" %>
<%@ page import="edu.ucla.cs.cs144.User" %>
<%@ page import="edu.ucla.cs.cs144.Bid" %>
<!DOCTYPE html>

<html>
<head>
    <title>Item Search Result</title>
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



    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
       <script>
function initMap() {
	<% Item item1 = (Item)request.getAttribute("item"); %>
	var latitude = "<%=item1.getLatitude()%>";
	var longitude = "<%=item1.getLongitude()%>";
	if(latitude!="Not Available" && longitude!="Not Available"){
		var map = new google.maps.Map(document.getElementById('map'), {
    	zoom: 8,
    	center: {lat: parseFloat(latitude), lng: parseFloat(longitude)}
  		});
  		var marker = new google.maps.Marker({
  		    map: map,
  		    position: {lat: parseFloat(latitude), lng: parseFloat(longitude)}
  			})
	}

	else{
			var map = new google.maps.Map(document.getElementById('map'), {
		    	zoom: 8,
		    	center: {lat: 0, lng: 0}
		  	});
		  	var geocoder = new google.maps.Geocoder();
			var address = "<%=item1.getLocation() %>, <%=item1.getCountry() %>";

			geocoder.geocode(
		                    {'address': address},
		                    function(results, status) {
		                        if (status == google.maps.GeocoderStatus.OK) {
		                            map.setCenter(results[0].geometry.location);
		                            map.fitBounds(results[0].geometry.viewport);
		                            map.setZoom(8);
		                            var marker = new google.maps.Marker({
		                                map: map,
		                                position: results[0].geometry.location});
		                        } else {
		                        	//use the longitude and latitude 
		                            geocoder.geocode( { 'address': "<%= "USA" %>"},
		                            function(results, status) {
		                                if (status == google.maps.GeocoderStatus.OK)
		                                {
		                                    map.setCenter(results[0].geometry.location);
		                                    map.fitBounds(results[0].geometry.viewport);
		                                    var marker = new google.maps.Marker({
		                                        map: map,
		                                        position: results[0].geometry.location});
		                                }
		                            })
		                        }
		                    }
	    );
	}
  
}


/*function geocodeAddress(geocoder, resultsMap) {
  var address = 
  geocoder.geocode({'address': address}, function(results, status) {
    if (status === google.maps.GeocoderStatus.OK) {
      resultsMap.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
        map: resultsMap,
        position: results[0].geometry.location
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });*/


</script>

</head>
<body onLoad="initMap()">
    <h1>Item Search Result</h1>
    
    <form method="get" action="./item">

    <div>
    	Item ID: <input type="text" name="id"><br><br>
    	<input type="submit" value="Search"><br>
	</div>

    <% Item item = (Item)request.getAttribute("item"); 
    %>
   <br><br>
   <div id="t">
    <table border="1" width = "100%" color="#330099">
	
		<tr>
			<td>ItemId</td><td><%= item.getID()%></td>
		</tr>
		<tr>
			<td>Name</td><td><%= item.getName()%></td>
		</tr>
<!-- 		<tr>
			<td>Categories</td><td><%= item.getCategories()[0]%><br><%= item.getCategories()[1]%></td>
		</tr> -->
		<%	
			out.println("<tr>");
			out.println("<td>Categories</td>");
			out.println("<td>");
			for(int i=0; i<item.getCategories().length; i++){
				out.println(item.getCategories()[i]);
				if(i+1<item.getCategories().length)	out.print("<br>");
			}
			out.println("/td");
			out.println("</tr>");
		%>
		<tr>
			<td>Currently</td><td><%= item.getCurrently()%></td>
		</tr>
		<tr>
			<td>BuyPrice</td><td><%= item.getBuyPrice()%></td>
		</tr>
		<tr>
			<td>FirstBid</td><td><%= item.getFirstBid()%></td>
		</tr>
		<tr>
			<td>NumOfBids</td><td><%= item.getNumOfBids()%></td>
		</tr>
		<tr>
			<td>Bids</td><td></td>
		</tr>
		<%
			for(int i=0; i<item.getBids().length;i++){
				out.println("<tr>");
				out.println("<td>Bid"+(i+1)+"</td>");
				out.println(item.getBids()[i].toHTTP());
				out.println("</tr>");
			}
		%>
		<tr>
			<td>Location</td><td><%= item.getLocation()%></td>
		</tr>
		<tr>
			<td>Latitude</td><td><%= item.getLatitude()%></td>
		</tr>
		<tr>
			<td>Longitude</td><td><%= item.getLongitude()%></td>
		</tr>
		<tr>
			<td>Country</td><td><%= item.getCountry()%></td>
		</tr>
		<tr>
			<td>Started</td><td><%= item.getStarted()%></td>
		</tr>
		<tr>
			<td>Ends</td><td><%= item.getEnds()%></td>
		</tr>
		<tr>
			<td>Seller</td><td>ID: <%= item.getSeller().getID()%><br>Rating: <%= item.getSeller().getRating()%></td>
		</tr>
		<tr>
			<td>Description</td><td><%= item.getDescription()%></td>
		</tr>
    </table> 
	</div>
    <br>
    
    <div id="map" style="width:600px; height:400px; margin-left: auto; margin-right: auto; margin-bottom:50px"></div>
 
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAfZZoh2CQ-iqhvsvJEs_remDqOjJ-LlZY&signed_in=true&callback=initMap"
        async defer></script>

</body>
</html>



