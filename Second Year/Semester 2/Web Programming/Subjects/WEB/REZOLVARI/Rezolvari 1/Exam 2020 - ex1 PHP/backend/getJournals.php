<?php
// send user through url
// SELECT * FROM `assets` WHERE userid = 1;

include 'C:\xampp\htdocs\exam2020ex1\backend\dbconnection.php';

//header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");


if (session_status() == PHP_SESSION_NONE) session_start();
if ($_SESSION['valid'] == 1) {

    global $conn;

//echo "username is: " . $_SESSION['username'];

    $rows = array();
    // // SELECT * FROM `assets` WHERE userid = 1;
    $selectQuery = "SELECT * FROM journal";
    //$filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
    $statement = $conn->prepare($selectQuery);
    $statement->execute();
    $resultSet = $statement->get_result();

    while ($row = mysqli_fetch_assoc($resultSet)) {

        $document = array();
        array_push($document, $row['id']);
        array_push($document, $row['name']);

        array_push($rows, $document);
    }


    echo json_encode($rows);
}
else {
    echo "this information is private...";
    echo "<form action='destroySession.php' method='POST'>";
    echo " 	 <input type='submit' value='Logout' name='logout' />";
    echo "</form>";
}


