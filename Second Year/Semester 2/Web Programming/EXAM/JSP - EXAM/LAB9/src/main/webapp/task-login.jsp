<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <html>

        <head>
            <title>Task Management - Login</title>
        </head>

        <body>
            <h1>Task Management System</h1>
            <h2>Login</h2>

            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>

            <form method="post" action="login">
                <label for="username">Username:</label><br>
                <input type="text" id="username" name="username" required><br><br>

                <input type="submit" value="Login">
            </form>

            <h3>Available Users:</h3>
            <p>alice, bob, charlie, diana</p>
        </body>

        </html>