<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static java.lang.System.out" %>
<%@ page import="com.example.domain.User" %>
<%@ page import="com.example.service.ChannelService" %>
<%@ page import="com.example.domain.Channel" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="jquery-2.0.3.js"></script>
    <script src="script.js"></script>
</head>
<body>

    <%
    String username = (String) session.getAttribute("Username");

//out.print(products);
    %>

<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand"> Welcome <%=username%>! This is your channel subscription app!</a>
</nav>

    <div class="d-flex align-items-center justify-content-center" >
        <label>
            <form  class="form-group" action="Channel" method="post">
                <select name="user">
                    <%
                        List<User> users = ChannelService.getUsers();
                    %>
                    <c:forEach items="<%=users%>" var="user">
                        <option value="${user.id}">${user.name}</option>
                    </c:forEach>
                </select>
                <br>
                <button id = "view" class="btn btn-primary"  name = "add">View Users</button>
            </form>
        </label>
    </div>
    <div>
        <table class = "table" id = "table-container"></table>
    </div>

    <div class="d-flex align-items-center justify-content-center" >
        <label>
            <form  class="form-group" action="Channel" method="post">
                <select name="channel">
                    <%
                        List<Channel> channels = ChannelService.getChannels();
                    %>
                    <c:forEach items="<%=channels%>" var="channel">
                        <option value="${channel.id}">${channel.name}</option>
                    </c:forEach>
                </select>
                <br>
                <button id = "subscribe" class="btn btn-primary"  name = "subscribe">Subscribe</button>
            </form>
        </label>
    </div>
</body>
</html>