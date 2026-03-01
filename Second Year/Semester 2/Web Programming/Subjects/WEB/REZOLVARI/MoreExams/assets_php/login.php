<?php
    require "connection.php";

    $conn = openConnection();

    $user = $_POST["username"];
    $pw = $_POST["password"];
    $result = mysqli_query($conn, "select * from users where username = '" . $user . "' and password = '" . $pw . "'");
    $row = mysqli_fetch_array($result);

    if ($row) {
        session_start();
        $_SESSION['userid'] = $row['id'];
        $_SESSION['username'] = $row['username'];
        header("Location: main.html");
    }
    else {
        echo "Could not login";
    }
    closeConnection($conn);
?>