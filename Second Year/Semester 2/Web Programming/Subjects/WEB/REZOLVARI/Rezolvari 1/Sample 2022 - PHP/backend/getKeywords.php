<?php
// SELECT * FROM `document` WHERE title LIKE "%oc%";

include 'C:\xampp\htdocs\exam2022sample\backend\dbconnection.php';

//header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");

global $conn;


$rows = array();

$selectQuery = "SELECT * FROM keyword ";
//$filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
$statement = $conn->prepare($selectQuery);
$statement->execute();
$resultSet = $statement->get_result();

while ($row = mysqli_fetch_assoc($resultSet)) {

    $document = array();

    array_push($document, $row['ID']);
    array_push($document, $row['kkey']);
    array_push($document, $row['value']);

    array_push($rows, $document);
}


echo json_encode($rows);

