<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <title>Hotel Rooms</title>
        </head>

        <body>
            <h1>Hotel Rooms</h1>
            <a href="/home">Back to Home</a> | <a href="/logout">Logout</a>

            <h2>Filter by Dates:</h2>
            <form method="get" action="/rooms">
                Check In: <input type="date" name="checkIn" value="${checkIn}">
                Check Out: <input type="date" name="checkOut" value="${checkOut}">
                <input type="submit" value="Filter Available Rooms">
            </form>

            <h2>Rooms:</h2>
            <table border="1">
                <tr>
                    <th>Room Number</th>
                    <th>Capacity</th>
                    <th>Base Price</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${rooms}" var="room">
                    <tr>
                        <td>${room.roomNumber}</td>
                        <td>${room.capacity}</td>
                        <td>${room.basePrice}</td>
                        <td>
                            <c:if test="${not empty checkIn and not empty checkOut}">
                                <form method="post" action="/reserve" style="display:inline">
                                    <input type="hidden" name="roomId" value="${room.id}">
                                    <input type="hidden" name="checkIn" value="${checkIn}">
                                    <input type="hidden" name="checkOut" value="${checkOut}">
                                    Guests: <input type="number" name="numberOfGuests" min="1" max="${room.capacity}"
                                        required>
                                    <input type="submit" value="Reserve">
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <c:if test="${empty rooms}">
                <p>No rooms available for the selected dates.</p>
            </c:if>
        </body>

        </html>