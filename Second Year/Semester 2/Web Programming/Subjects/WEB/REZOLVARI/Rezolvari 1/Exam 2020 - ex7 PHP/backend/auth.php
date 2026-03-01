<?php
if (session_status() == PHP_SESSION_NONE) session_start();
include 'C:\xampp\htdocs\exam2020ex7\dbconnection.php';

header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");

global $conn;
//echo $_GET['username'];
var_dump($_SESSION);
//echo $_SESSION['username'];

if (!isset($_SESSION['valid'])) {
    echo "hey";
} else {
    echo "this information is private...";
    echo "<form action='destroy_session.php' method='POST'>";
    echo " 	 <input type='submit' value='Logout' name='logout' />";
    echo "</form>";
}

