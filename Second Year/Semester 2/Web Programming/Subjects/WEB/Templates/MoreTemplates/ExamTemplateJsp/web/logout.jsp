<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logout page</title>
</head>
<body>

<h3>You have been successfully logout</h3>

<p>
    <%
        mvcIntelliJIdea.model.User user = (mvcIntelliJIdea.model.User) request.getAttribute("user");
        out.println("Hope to see you soon, " + user.getUsername() + "!");
    %>

</p>

<button><a href="login.html" style="text-decoration: none; ">Back to login</a></button>

</body>
</html>
