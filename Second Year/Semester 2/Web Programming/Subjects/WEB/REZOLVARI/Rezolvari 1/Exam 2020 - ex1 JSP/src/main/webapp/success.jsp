<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static java.lang.System.out" %>
<%@ page import="com.example.model.Journal" %>
<%@ page import="com.example.Service.JournalService" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="jquery-2.0.3.js"></script>
    <script src = "script.js"></script>
    <script src="random.js"></script>
</head>
<body>

<%
    String username = (String) session.getAttribute("Username");
    //Integer id = (Integer) session.getAttribute("Id");

    //System.out.println(username + " " + id);

    if(username == null){
        response.sendRedirect("index.jsp");
        return;
    }
//out.print(products);
%>

<nav class="navbar navbar-light bg-light">
    <a class="navbar-brand"> Welcome <%=username%>! This is your journal app!</a>

    <%-- Logout --%>
    <form class="form-inline" action="Logout" method="post">
        <button class="btn btn-outline-secondary my-2 my-sm-0" type="submit" name="logout" value="Logout">Logout</button>
    </form>
</nav>


<label>
    <form  class="form-group" action="Journal" method="post">
        <select name="category">
            <%
                List<Journal> journals = JournalService.getJournals();
            %>
            <c:forEach items="<%=journals%>" var="category">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </select>
        <br>
        <input type="submit" value="Submit" name = "dropdownAction">
        <button id = "viewJournal">View</button>
    </form>
</label>

<div>
    <table id = "table-container" class = "table"></table>
</div>

<div class="d-flex align-items-center justify-content-center">
    <form  class="form-group" action="Journal" method="post">
        <div class="form-group">
            <label for="artname">Article Name</label>
            <input type="text" name="artname" class="form-control" id="artname" aria-describedby="emailHelp" placeholder="Enter article name ..." required>

            <label for="description">Summary</label>
            <input type="text" name="description" class="form-control" id="description" aria-describedby="emailHelp" placeholder="Enter description ..." required>

            <label for="jname">Journal name</label>
            <input type="text" name="jname" class="form-control" id="jname" aria-describedby="emailHelp" placeholder="Enter journal name ..." required>
        </div>

        <button type="submit" class="btn btn-primary"  name = "add">Add Journal</button>
    </form>
</div>

<button class="btn btn-primary" id = "get">Get Last Journal</button>

<script>
    var username = "<%=username%>";

    $(document).ready(

        function (){
            notifyNewArticle();
            $(document).on('click', '#get', function(e){
                e.preventDefault();
                getArticles();
            });
        }
    );
</script>
