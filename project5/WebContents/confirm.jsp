<!DOCTYPE html>
<html>
	<head>
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
            #t{
				position: fixed;
				left: calc(35%);
            }
            #a{
            	position: relative;
            	top: 150px;
            }
		</style>
		<title>Confirmation</title>	
	</head>
	<body>
		<div>
			<h1>Thanks for your payment!</h1>
			<table border="1" width = "30%" color="#330099" id="t">
				<tr>
					<td>Item ID</td><td><%= request.getAttribute("id")%></td>
				</tr>
				<tr>
					<td>Name</td><td><%= request.getAttribute("name")%></td>
				</tr>
				<tr>
					<td>Price</td><td><%= request.getAttribute("price")%></td>
				</tr>
				<tr>
					<td>Credit Card Number</td><td><%= request.getAttribute("number")%></td>
				</tr>
			</table>
			<% String homeURL="http://" + request.getServerName() + ":1448" + request.getContextPath() + "/index.html";%>
			<a class="one" href="<%=homeURL%>" id="a">Back to home page</a>
		</div>
	</body>
</html>