<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <title>Hotel Login</title>
        </head>

        <body>
            <h1>Hotel Reservation System - Login</h1>

            <c:if test="${not empty error}">
                <p style="color:red">${error}</p>
            </c:if>

            <form method="post" action="/login">
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" required></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="number" name="password" required></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="Login">
                        </td>
                    </tr>
                </table>
            </form>