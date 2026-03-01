
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View assets</title>
    <link rel="stylesheet" href="stylesheet.css">
</head>

<%
    String table = (String) request.getAttribute("table");
    int id = (int) request.getAttribute("id");
%>
<body>

<!--Main content of the page-->
<h1>Assets database. UserId = <%=id%></h1>
<%= table %>
<br>

<a href = "${pageContext.request.contextPath}/AddController?userId=<%=id%>"><button>Add new assets</button></a><br>
<br>

<!--Logout button-->
<div class="button" id="logout">
    <a href="LogoutController">
        <button>Logout</button>
    </a>
</div>

</body>
</html>
