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
include 'C:\xampp\htdocs\exam2020ex7\dbconnection.php';

global $conn;
// $author_id = isset($recipe['authorId'])?$recipe['authorId']:1; //i
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $json = file_get_contents('php://input');
    $user = json_decode($json, true);
    $username = $user['username'];
    $password = $user['password'];
//    $username = //$_POST['username'];
//    $password = $_POST['password'];
    $selectQuery = $conn->prepare("SELECT * FROM users WHERE username = ? and password = ? LIMIT 1");
    $selectQuery->bind_param("ss", $username, $password);
    $selectQuery->execute();
//
    $resultSet = $selectQuery->get_result();
    while($row = mysqli_fetch_assoc($resultSet)) {
//        echo $row;
        $dbUsername = $row['username'];
        //echo $row['password'];
//
        if($username == $dbUsername){
            $_SESSION['username'] = $username;
            $_SESSION['valid'] = 1;
            //var_dump($_SESSION);
            if($password == $row['password']){
            echo json_encode($username);
            return;}
//            header('Location: ../Pagination/Pagination.php');
            else{
                echo "error";
            return;}
        }
        else {
            echo "error";
        }
    }
    echo "fail";
}

?>