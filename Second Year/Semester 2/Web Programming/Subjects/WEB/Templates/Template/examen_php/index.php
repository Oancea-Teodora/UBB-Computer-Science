<?php
session_start(); 

// $con = mysqli_connect('localhost', 'root', '', 'webexamen') or die('Unable to connect to the database.');

// if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['login'])) {
//     $username = mysqli_real_escape_string($con, $_POST['username']);

//     $query = "SELECT id, name, secretQuestion FROM user WHERE name = ?";
//     $stmt = mysqli_prepare($con, $query);
//     mysqli_stmt_bind_param($stmt, "s", $username);
//     mysqli_stmt_execute($stmt);
//     $result = mysqli_stmt_get_result($stmt);
//     $row = mysqli_fetch_assoc($result);

//     if ($row) {
//         $_SESSION['username'] = $row['name'];
//         $_SESSION['userId'] = $row['id'];
//         $_SESSION['question'] = $row['secretQuestion'];
//         header("Location: question.php");
//         exit;
//     } else {
//         $_SESSION['error_message'] = 'Invalid username.';
//         header("Location: index.php"); 
//         exit;
//     }
// }

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<div class="login-container">
    <h2>Login</h2>
    <form action="login.php" method="POST">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>
        <button type="submit" name="login">Login</button>
    </form>
</div>
</body>
</html>
