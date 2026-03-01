<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<body>
<h2>Delete movie</h2>
<form action="movie" method="post">
  Title: <input type="text" name="title" value="${title}" />
  <input type="submit" value="Delete" />
</form>

<p>Movies</p>
<ul>
  <c:forEach var="d" items="${movies}">
    <li>
      <strong>#${d.id} – ${d.title}</strong><br/>
        ${d.duration} minutes<br/>
    </li>
  </c:forEach>
</ul>


<p><a href="home">Home</a></p>
</body>

</html>