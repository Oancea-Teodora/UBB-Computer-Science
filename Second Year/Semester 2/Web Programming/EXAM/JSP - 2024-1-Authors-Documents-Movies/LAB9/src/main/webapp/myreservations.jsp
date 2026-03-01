<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <body>
        <h2>My Reservations</h2>
        <table border="1">
            <tr>
                <th>Room</th>
                <th>In</th>
                <th>Out</th>
                <th>Guests</th>
                <th>Price</th>
            </tr>
            <c:forEach var="r" items="${myRes}">
                <tr>
                    <td>${r.roomNumber}</td>
                    <td>${r.checkInDate}</td>
                    <td>${r.checkOutDate}</td>
                    <td>${r.numberOfGuests}</td>
                    <td>${r.totalPrice}</td>
                </tr>
            </c:forEach>
        </table>
        <p><a href="home">Home</a></p>
    </body>

    </html>