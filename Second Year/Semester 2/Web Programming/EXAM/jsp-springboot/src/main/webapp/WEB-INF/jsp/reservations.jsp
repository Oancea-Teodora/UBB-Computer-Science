<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <title>My Reservations</title>
        </head>

        <body>
            <h1>My Reservations</h1>
            <a href="/home">Back to Home</a> | <a href="/logout">Logout</a>

            <c:if test="${not empty message}">
                <p style="color:green">${message}</p>
            </c:if>

            <table border="1">
                <tr>
                    <th>Room Number</th>
                    <th>Check In</th>
                    <th>Check Out</th>
                    <th>Guests</th>
                    <th>Total Price</th>
                </tr>
                <c:forEach items="${reservations}" var="reservation">
                    <tr>
                        <td>${reservation.room.roomNumber}</td>
                        <td>${reservation.checkInDate}</td>
                        <td>${reservation.checkOutDate}</td>
                        <td>${reservation.numberOfGuests}</td>
                        <td>${reservation.totalPrice}</td>
                    </tr>
                </c:forEach>
            </table>

            <c:if test="${empty reservations}">
                <p>No reservations found.</p>
            </c:if>
        </body>

        </html>