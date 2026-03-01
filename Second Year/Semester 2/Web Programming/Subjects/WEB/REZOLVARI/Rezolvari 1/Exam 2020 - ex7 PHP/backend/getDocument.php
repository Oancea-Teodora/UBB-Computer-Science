<?php


if (session_status() == PHP_SESSION_NONE) session_start();
if($_SESSION['valid'] == 1) {

    include 'C:\xampp\htdocs\exam2020ex7\dbconnection.php';

    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Credentials: true");
//header('Access-Control-Allow-Origin: *');
    header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");

    global $conn;

//echo "username is: " . $_SESSION['username'];

    $rows = array();
    $id = $_GET['id'];

// SELECT * FROM `documents` WHERE id = 1;
    $selectQuery = "SELECT * FROM documents WHERE id = ?";
    $statement = $conn->prepare($selectQuery);
    $statement->bind_param("i", $id);
    $statement->execute();
    $resultSet = $statement->get_result();

    $row = mysqli_fetch_assoc($resultSet);


    $document = array();
    array_push($document, $row['id']);
    array_push($document, $row['websiteId']);
    array_push($document, $row['name']);
    array_push($document, $row['keyword1']);
    array_push($document, $row['keyword2']);
    array_push($document, $row['keyword3']);
    array_push($document, $row['keyword4']);
    array_push($document, $row['keyword5']);


    echo json_encode($document);
}

?>