<?php
if (session_status() == PHP_SESSION_NONE) session_start();
header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
header("Access-Control-Allow-Origin: http://localhost:4200");

header("Access-Control-Allow-Credentials: true");
//header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");
header("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept");
header("Access-Control-Allow-Credentials: true");
include 'C:\xampp\htdocs\exam2022sample\backend\dbconnection.php';

global $conn;
// $author_id = isset($recipe['authorId'])?$recipe['authorId']:1; //i

$json = file_get_contents('php://input');
$user = json_decode($json, true);
//    $username = $user['username'];
$username = $_POST['username'];
//    $password = $_POST['password'];
$selectQuery = $conn->prepare("SELECT * FROM softwaredeveloper WHERE name = ?");
$selectQuery->bind_param("s", $username);
$selectQuery->execute();
//
$resultSet = $selectQuery->get_result();
while($row = mysqli_fetch_assoc($resultSet)) {
//        echo $row;
    $dbUsername = $row['name'];
    //echo $dbUsername;
    //echo $row['password'];
//
    if($username == $dbUsername){
        $_SESSION['username'] = $username;
        $_SESSION['valid'] = 1;
        //var_dump($_SESSION);
        $result = $username . ';' . $row['id'];
        echo json_encode($result);
        return;
    }
    else {
        echo "error";
    }
}
echo "fail";


?>