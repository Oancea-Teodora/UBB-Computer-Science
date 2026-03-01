<?php session_start(); ?>
<!DOCTYPE html>
<html>
<head>
    <title>Login Form</title>
</head>
<link rel="stylesheet" href="styles.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#login-btn").click(function(){
            $.ajax({
                url: 'http://localhost/Examen_Template/login.php',
                type: 'POST',
                data: form.serialize(),
                success: function(response) {
                    console.log(response);
                }
            });
        });
    });
</script>
<body>
    <h2>Login</h2>
    <form method="POST" action="login.php">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>
        <input id="login-btn" type="submit" name="login" value="Login">
        <p id="message-login"></p>
    </form>
</body>
</html>