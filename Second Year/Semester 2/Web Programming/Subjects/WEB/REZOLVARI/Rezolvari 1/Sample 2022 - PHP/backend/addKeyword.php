<?php
include 'C:\xampp\htdocs\exam2022sample\backend\dbconnection.php';


header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");

global $conn;


//if($_SERVER['REQUEST_METHOD'] == 'POST'){

$json = file_get_contents('php://input');
$keyword = json_decode($json, true);

//$recipe = json_decode(file_get_contents("php://input"), true);
$key = $keyword['key'];
$value = $keyword['value'];

$insertQuery = $conn->prepare("INSERT INTO keyword(kkey, value) VALUES(?, ?)");
$insertQuery->bind_param("ss", $key, $value);
$insertQuery->execute();

$result = $insertQuery->affected_rows;
//$result = $insertQuery->get_result();

if($result){
    echo json_encode("Nice");
}

//return addPopUp("Successfully added!");
//}else{
//return addPopUp("Failure");
//}

?>