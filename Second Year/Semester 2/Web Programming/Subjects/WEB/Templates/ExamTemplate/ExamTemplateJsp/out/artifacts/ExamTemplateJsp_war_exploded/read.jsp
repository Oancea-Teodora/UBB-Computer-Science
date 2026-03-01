
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View all students</title>
    <link rel="stylesheet" href="stylesheet.css">
</head>

<% String table = (String) request.getAttribute("table"); %>

<body>

<!--Main content of the page-->
<h1>Students database</h1>
<%= table %>
<br>

<a href = "AddController"><button>Add new student</button></a><br>
<a href = "search.jsp"><button>Search students</button></a><br>
<a href = "ReadController"><button>View all students</button></a>
<br><br><br>

<!--Logout button-->
<div class="button" id="logout">
    <a href="LogoutController">
        <button>Logout</button>
    </a>
</div>

</body>
</html>
