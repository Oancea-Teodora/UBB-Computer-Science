<%@ page language="java" contentType="text/html; charset=UTF-8" %>
    <html>

    <head>
        <title>Login</title>
    </head>

    <body>
        <h2>Login</h2>
        <form method="post" action="login">
            Username: <input type="text" name="username" /><br />
            Password: <input type="password" name="password" /><br />
            <input type="submit" value="Log In" />
        </form>
        <% String msg=(String) request.getAttribute("error"); if (msg !=null) { %>
            <p style="color:red;">
                <%= msg %>
            </p>
            <% } %>
    </body>

    </html>