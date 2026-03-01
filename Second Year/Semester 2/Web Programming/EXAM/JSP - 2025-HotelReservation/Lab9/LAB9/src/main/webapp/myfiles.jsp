<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <body>
        <h2>My Files</h2>
        <table>
            <tr>
                <th>Name</th>
            </tr>
            <c:forEach var="r" items="${myRes}">
                <tr>
                    <td>${r}</td>
                </tr>
            </c:forEach>
        </table>
        <p><a href="home">Home</a></p>

    <p> Document with largest number of authors: </p>
    <p> Document name: ${mostAuthors}</p>
    </body>

    </html>