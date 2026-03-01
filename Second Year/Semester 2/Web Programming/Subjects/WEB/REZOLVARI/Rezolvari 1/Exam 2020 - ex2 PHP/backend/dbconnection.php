<?php

$servername = "localhost";
$username = "root";
$password = "doareu123";
$dbname = "channels";

$conn = mysqli_connect(
    $servername,
    $username,
    $password,
    $dbname
);

if($conn->connect_error){
    die("Connection to the database failed" . $conn->connect_error);
}
