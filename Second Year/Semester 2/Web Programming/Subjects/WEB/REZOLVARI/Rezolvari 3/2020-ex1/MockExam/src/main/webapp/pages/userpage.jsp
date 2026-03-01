<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Jsp Exam</title>
    <link rel="stylesheet" href="../styles/userpage.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="../scripts/userpage.js"></script>
</head>
<body>
<h1><%= "Jsp Exam" %></h1>
<hr>
<%
    HttpSession currentSession = request.getSession();
    Object obj = currentSession.getAttribute("user");
    String name = "[error]";

    if ((obj instanceof String) && !((String) obj).equals("")) {
        name = ((String) request.getSession().getAttribute("user"));
    }
%>
<h1 id="title">Hello, <%=name%>!</h1>

<label>
    Journal name: <input type="text" id="journal-name" name="journal-name"><br>
</label>
<button onclick="getAllArticlesFromJournalBtnClicked()">Submit journal name</button><br>

<br>
<hr>
<br>

<label>
    Article summary: <input type="text" id="article-summary" name="article-summary"><br>
</label>
<button onclick="addArticleBtnClicked()">Add article</button><br>

</body>
</html>
