<?php
// send user through url
// SELECT * FROM `assets` WHERE userid = 1;

include 'C:\xampp\htdocs\exam2020sample\backend\dbconnection.php';

//header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");


if (session_status() == PHP_SESSION_NONE) session_start();
if ($_SESSION['valid'] == 1) {

    global $conn;

    $json = file_get_contents('php://input');
    $document = json_decode($json, true);
    //var_dump($document);
    foreach ($document as $item) {
        $userid = (array_values($item)[1]);
        $name = (array_values($item)[2]);
        $description = (array_values($item)[3]);
        $value = (array_values($item)[4]);

        // insert in table -INSERT INTO assets( userid, name, description, value) VALUES ()
        $insertQuery = $conn->prepare("INSERT INTO assets( userid, name, description, value) VALUES (?, ?, ?, ?)");
        $insertQuery->bind_param("issi", $userid, $name, $description, $value);
        $insertQuery->execute();

    }
    echo json_encode("ok");
}
else {
    echo "this information is private...";
    echo "<form action='destroySession.php' method='POST'>";
    echo " 	 <input type='submit' value='Logout' name='logout' />";
    echo "</form>";
}
