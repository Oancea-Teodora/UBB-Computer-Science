<?php
require "connection.php";

$connection = open_connection();

session_start();
$_SESSION['username'] =  $_POST["username"];
header("Location: main.html");

close_connection($connection);
