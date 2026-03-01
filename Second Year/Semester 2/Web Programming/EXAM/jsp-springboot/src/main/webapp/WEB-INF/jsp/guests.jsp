<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <title>Total Guests</title>
        </head>

        <body>
            <h1>Total Guests in Hotel</h1>
            <a href="/home">Back to Home</a> | <a href="/logout">Logout</a>

            <h2>Search by Date:</h2>
            <form method="get" action="/guests">
                Date: <input type="date" name="date" value="${searchDate}" required>
                <input type="submit" value="Search">
            </form>

            <c:if test="${not empty totalGuests}">
                <h2>Results:</h2>
                <p>Total guests on ${searchDate}: <strong>${totalGuests}</strong></p>
            </c:if>
        </body>

        </html>