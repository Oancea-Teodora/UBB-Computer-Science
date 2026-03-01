<?php
session_start();
$con = mysqli_connect('localhost', 'root', '', 'webexamen') or die('Unable to connect to the database.');

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['login'])) {
    $username = mysqli_real_escape_string($con, $_POST['username']);

    $query = "SELECT id, name, secretQuestion, secretAnswer FROM user WHERE name = ?";
    $stmt = mysqli_prepare($con, $query);
    mysqli_stmt_bind_param($stmt, "s", $username);
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);
    $row = mysqli_fetch_assoc($result);

    if ($row) {
        $_SESSION['username'] = $row['name'];
        $_SESSION['userId'] = $row['id'];
        $_SESSION['question'] = $row['secretQuestion'];
        $_SESSION['secretAnswer'] = $row['secretAnswer'];
        header("Location: question.php");
        exit;
    } else {
        $_SESSION['error_message'] = 'Invalid username.';
        header("Location: index.php"); // Redirect back to the login page
        exit;
    }
}
?>