<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <title>Hotel Home</title>
        </head>

        <body>
            <h1>Welcome, ${user.username}!</h1>

            <h2>Menu:</h2>
            <ul>
                <li><a href="/rooms">View Available Rooms</a></li>
                <li><a href="/reservations">My Reservations</a></li>
                <li><a href="/guests">View Total Guests</a></li>
                <li><a href="/logout">Logout</a></li>
            </ul>
        </body>

        </html>