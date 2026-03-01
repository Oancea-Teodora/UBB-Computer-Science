<?php session_start(); ?>
<!DOCTYPE html>
<html>
<head>
    <title>Login Form</title>
</head>

<body>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/dompurify/2.2.8/purify.min.js"></script>

    <div class="table_container">
        <h1>Games Dashboard</h1>
        <div id="table"></div>
    </div>

    <h2>Add New Game</h2>
    <form id="addGameForm">
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
    <script>
        //$(document).ready(function () {

        //    $.get("/Main/GetGames",
        //        function (data, status) {
        //            $("#table").html(data);
        //        });
        //
        $(document).ready(function () {
            // Load games when the page loads using $.ajax
            $.ajax({
                url: "http://localhost/Examen_Template/getgames.php",
                type: "GET",
                success: function (data) {
                    $("#table").html(data);

                },
                error: function (xhr, status, error) {
                    alert("Error fetching games: " + xhr.responseText);
                }
            });
        });
        $("#submitButt").click(function () { 
            $.ajax({
                url: "http://localhost/Examen_Template/addgame.php",
                type: "POST",
                data: { 
                    title: $("#title").val(),
                    genre: $("#genre").val(),
                    release_date: $("#releaseDate").val(),
                    platform: $("#platform").val()
                },
                success: function(response) {
                    alert(response);
                }
            });
        });

    </script>

</body>
</html>
