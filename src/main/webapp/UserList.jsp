<!DOCTYPE html>
<html lang="en">
<head>
<title>UserList</title>
</head>
<body>
	<%
		String vale = (String) request.getAttribute("message");
		
		response.getWriter().println(vale);
	%>
	


</body>
</html>