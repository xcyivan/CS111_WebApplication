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
            #f{
            	position: relative;
            	top: 100px;
            }
		</style>
		<title>Payment Page</title>	
	</head>
	<body>
		<script>
			function check_number(card)
			{
				var regex = /^[- 0-9]*$/;
				if (regex.test(card.number.value)==false)
				{
					alert("Card number should consists of number, dash, and space only.");
					card.number.focus();
					return false;
				}
				if (card.number.value=="")
				{
					alert("Card number cannot be left empty.");
					card.number.focus();
					return false;
				}
				return true;
			}
		</script>
		<div>
			<h1>Buy Item</h1>
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
			</table>
			<% String secureURL="https://" + request.getServerName() + ":8443" + request.getContextPath() + "/session";%>
			<form method="POST" action="<%=secureURL%>" id="f" onSubmit="return check_number(this)">
				Credit Card Number:<input type="text" name="number"><br><br>
				<input type="hidden" name="id" value="<%= request.getAttribute("id")%>">
				<input type="hidden" name="flag" value="confirm">
				<input type="submit" value="Confirm"><br>
			</form>
		</div>
	</body>
</html>