<!DOCTYPE html>
<html lang="en">
<head>
<title>Chat</title>
<link rel="stylesheet" href="style.css">

</head>
	
<body>
	
	<%
		String vale = (String) request.getAttribute("message");
		
		response.getWriter().println(vale);
	%>
		


	




</body>
</html>