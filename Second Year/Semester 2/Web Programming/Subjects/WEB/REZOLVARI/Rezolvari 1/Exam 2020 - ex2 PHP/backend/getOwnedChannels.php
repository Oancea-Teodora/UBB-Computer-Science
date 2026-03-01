<?php
// send user through url
// SELECT * FROM `assets` WHERE userid = 1;

include 'C:\xampp\htdocs\exam2020ex2\backend\dbconnection.php';

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
    $userId = $_GET['id'];
    $selectQuery = "SELECT * FROM channels where ownerId = ?";
    //$filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
    $statement = $conn->prepare($selectQuery);
    $statement->bind_param("i", $userId);
    $statement->execute();
    $resultSet = $statement->get_result();

    while ($row = mysqli_fetch_assoc($resultSet)) {

        $document = array();
        array_push($document, $row['id']);
        array_push($document, $row['ownerId']);
        array_push($document, $row['name']);
        array_push($document, $row['description']);
        array_push($document, $row['subscribers']);

//        var_dump(json_decode($row['subscribers']));
//        echo "<br>";

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


