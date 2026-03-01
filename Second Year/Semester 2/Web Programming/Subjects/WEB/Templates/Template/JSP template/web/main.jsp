<%@ page import="template.domain.Game" %>
<%@ page import="template.domain.User" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
//    User user = (User) session.getAttribute("user");
//    if (user == null || user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
//        response.sendRedirect(request.getContextPath() + "/");
//        return;
//    }

%>

<html>
<head>
    <title>Games Dashboard</title>
</head>
<body>
<h1>Games Dashboard</h1>
<table>
    <tr>
        <th>Title</th>
        <th>Genre</th>
        <th>Release Date</th>
        <th>Platform</th>
    </tr>

</table>

<div id = "table-games">

</div>
<button  id="getAllGames">Get Games</button>
<h2>Add New Game</h2>
<form action="GameController" method ="post" id="addGameForm">
    <div class="form-group">
        <label for="title">Title:</label>
        <input type="text" class="form-control" id="title" name="title" required>
    </div>
    <div class="form-group">
        <label for="genre">Genre:</label>
        <input type="text" class="form-control" id="genre" name="genre" required>
    </div>
    <div class="form-group">
        <label for="releaseDate">Release Date:</label>
        <input type="date" class="form-control" id="releaseDate" name="releaseDate" required>
    </div>
    <div class="form-group">
        <label for="platform">Platform:</label>
        <input type="text" class="form-control" id="platform" name="platform" required>
    </div>
    <button type="submit" id="submitButt" class="btn btn-primary">Add Game</button>
</form>

<div id="message-add"></div>
<script src="js/jquery-2.0.3.js"></script>
<script src="js/ajax-utils.js"></script>
<script>
    $(document).ready(function(){
        $("#getAllGames").click(function() {
            $.ajax({
                url: 'GameController',
                data: {
                    action: 'getGames',

                },
                type: 'GET',
                dataType: 'json',
                success: function(games) {
                    console.log(games);
                    $("#table-games").html("");
                    $("#table-games").append("<tr><td>Title</td><td>Genre</td><td>Release Date</td><td>Platform</td></tr>");
                    for(var ob in games) {
                        $("#table-games").append("<tr>" +
                            "<td>"+games[ob].title+"</td>" +
                            "<td>"+games[ob].genre+"</td>" +
                            "<td>"+games[ob].releaseDate+"</td>" +
                            "<td><button class='selectGame' data-flight-id='" + games[ob].id + "'</button></tr>");
                    }
                },
                error: function() {
                    console.log('Error occurred');
                }
            });
        });
        $(document).ready(function() {
            $('#addGameForm').submit(function(event) {
                event.preventDefault();  // Prevent the form from submitting via the browser

                var formData = {
                    title: $('#title').val(),
                    genre: $('#genre').val(),
                    releaseDate: $('#releaseDate').val(),
                    platform: $('#platform').val(),
                    action: 'AddGame'
                };

                $.ajax({
                    type: 'POST',
                    url: 'GameController',
                    data: formData,
                    success: function(response) {
                        console.log('Game added successfully');
                        $('#message-add').text('Game added successfully');
                        $('#getAllGames').click();  // Refresh the games list
                    },
                    error: function() {
                        console.log('Error adding game');
                        $('#message-add').text('Error adding game');
                    }
                });
            });
    }); });
</script>
</body>
</html>
