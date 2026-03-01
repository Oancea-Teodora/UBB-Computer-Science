
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Main page</title>
</head>
<body>
<!--Welcome message-->
<div>
    <h1>
        <%
            mvcIntelliJIdea.model.User user = (mvcIntelliJIdea.model.User) request.getAttribute("user");
            out.println("Welcome, " + user.getUsername() + "!");
        %>
    </h1>
</div>

<!--Main content of the page-->
<h2>Students database</h2>
<a href = "ReadController"><button>View all students</button></a>
<br>
<a href = "search.jsp"><button>Search students</button></a>
<br><br>

<!--Logout button-->
<div class="button" id="logout">
    <a href="LogoutController">
        <button>Logout</button>
    </a>
</div>
</body>
</html>
