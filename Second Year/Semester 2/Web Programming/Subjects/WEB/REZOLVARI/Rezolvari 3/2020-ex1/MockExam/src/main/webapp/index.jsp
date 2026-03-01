<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Jsp Exam</title>
    <link rel="stylesheet" href="styles/main.css">
    <script src="scripts/main.js"></script>
</head>
<body>
<h1><%= "Jsp Exam" %></h1>
<hr>
    <%
        HttpSession currentSession = request.getSession();
        Object obj = currentSession.getAttribute("user");
        String status = "Not logged in before";

        if ((obj instanceof String) && !((String) obj).equals("")) {
            String name = ((String) request.getSession().getAttribute("user"));
            status = "Logged in before as: " + name;
        }
    %>
    <h1 id="title">User status: <%=status%>!</h1>

    <label>
        Name: <input type="text" id="user-name" name="user-name"><br>
    </label>
    <button onclick="onSubmitNameBtnClicked()">Submit name</button><br>
</body>
</html>
