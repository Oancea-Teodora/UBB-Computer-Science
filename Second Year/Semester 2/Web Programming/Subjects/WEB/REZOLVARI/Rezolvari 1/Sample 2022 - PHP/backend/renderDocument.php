<?php

include 'C:\xampp\htdocs\exam2022sample\backend\dbconnection.php';

//header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");

global $conn;


$rows = array();
$filter = $_GET['id'];
$selectQuery = "SELECT * FROM `document` WHERE id = ?";
//$filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
$statement = $conn->prepare($selectQuery);
$statement->bind_param("s", $filter);
$statement->execute();
$resultSet = $statement->get_result();

while ($row = mysqli_fetch_assoc($resultSet)) {

    $document = array();

    //echo $row['listOfTemplates'];
    $str_arr = explode (",", $row['listOfTemplates']);
    //print_r($str_arr);

    foreach($str_arr as $item){
        //echo $item;
        // select the template with the given id
        // SELECT * FROM `template` WHERE id = 1;
        $selectQuery = "SELECT * FROM `template` WHERE id = ?";
        $statement = $conn->prepare($selectQuery);
        $statement->bind_param("s", $item);
        $statement->execute();
        $resultSet2 = $statement->get_result();
        $row2 = mysqli_fetch_assoc($resultSet2);

        array_push($rows, $row2['textContent']);
    }
//    array_push($document, $row['ID']);
//    array_push($document, $row['title']);
//    array_push($document, $row['listOfTemplates']);
//
//    array_push($rows, $document);
}


echo json_encode($rows);
