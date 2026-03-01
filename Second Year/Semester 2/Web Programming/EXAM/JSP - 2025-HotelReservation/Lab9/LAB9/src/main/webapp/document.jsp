<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <body>
        <h2>Add document</h2>
        <form action="document" method="post">
            Name: <input type="text" name="name" value="${name}" />
            Content: <input type="text" name="content" value="${content}" />
            <input type="submit" value="Add" />
        </form>

            <p>Documents</p>
            <ul>
                <c:forEach var="d" items="${documents}">
                    <li>
                        <strong>#${d.id} – ${d.name}</strong><br/>
                            ${d.contents}
                    </li>
                </c:forEach>
            </ul>


        <p><a href="home">Home</a></p>
    </body>

    </html>