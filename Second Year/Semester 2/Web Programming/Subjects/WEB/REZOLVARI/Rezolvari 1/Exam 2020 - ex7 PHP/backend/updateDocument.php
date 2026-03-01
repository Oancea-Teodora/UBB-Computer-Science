<?php

include 'C:\xampp\htdocs\exam2020ex7\dbconnection.php';

header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With, Authorization");
header("Access-Control-Allow-Origin: http://localhost:4200");
// header("Access-Control-Allow-Origin:  http://127.0.0.1:3000");
header("Access-Control-Allow-Credentials: true");
//http://localhost:4200'

global $conn;
if (session_status() == PHP_SESSION_NONE) session_start();
//var_dump($_SESSION);
if($_SESSION['valid'] == 1) {



    $json = file_get_contents('php://input');
    $document = json_decode($json, true);

    $id = $document['id'];
    $websiteId = $document['websiteId'];
    $name = $document['name'];
    $keyword1 = $document['keyword1'];
    $keyword2 = $document['keyword2'];
    $keyword3 = $document['keyword3'];
    $keyword4 = $document['keyword4'];
    $keyword5 = $document['keyword5'];


    $updateQuery = $conn->prepare("UPDATE documents SET websiteId=?,name=?,keyword1=?,keyword2=?,keyword3=?,keyword4=?,keyword5=? WHERE id = ?");
    $updateQuery->bind_param("issssssi", $websiteId, $name, $keyword1, $keyword2, $keyword3, $keyword4, $keyword5, $id);
    $updateQuery->execute();

    $result = $updateQuery->affected_rows;

    if ($result) {
        echo json_encode("Document updated successfully");
    }
}
else{
    echo "heu";
}

?>