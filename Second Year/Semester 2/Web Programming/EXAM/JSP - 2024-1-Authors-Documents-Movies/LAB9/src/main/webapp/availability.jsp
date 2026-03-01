<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <body>
        <h2>Find Room</h2>
        <form action="availability" method="post">
            In: <input type="date" name="checkIn" value="${checkIn}" />
            Out: <input type="date" name="checkOut" value="${checkOut}" />
            <input type="submit" value="Search" />
        </form>

        <c:if test="${not empty freeRooms}">
            <p>Guests on ${checkIn}: ${guestCount}</p>
            <table border="1">
                <tr>
                    <th>Room</th>
                    <th>Cap</th>
                    <th>Price</th>
                    <th>Reserve</th>
                </tr>
                <c:forEach var="r" items="${freeRooms}">
                    <tr>
                        <td>${r.roomNumber}</td>
                        <td>${r.capacity}</td>
                        <td>${r.basePrice}</td>
                        <td>
                            <form action="reserve" method="post">
                                <input type="hidden" name="roomId" value="${r.id}" />
                                <input type="hidden" name="checkIn" value="${checkIn}" />
                                <input type="hidden" name="checkOut" value="${checkOut}" />
                                Guests: <input name="guests" size="2" value="1" />
                                <input type="submit" value="Reserve" />
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <p><a href="home">Home</a></p>
    </body>

    </html>