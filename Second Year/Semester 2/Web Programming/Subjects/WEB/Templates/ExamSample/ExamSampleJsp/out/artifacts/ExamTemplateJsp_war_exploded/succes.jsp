
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
<h2>Assets database</h2>
<div class="button">
<a href = "${pageContext.request.contextPath}/ReadController?userId=<%=user.getId()%>"><button>View all assets</button></a>
</div>
<br>

<!--Logout button-->
<div class="button" id="logout">
    <a href="LogoutController">
        <button>Logout</button>
    </a>
</div>

</body>
</html>
