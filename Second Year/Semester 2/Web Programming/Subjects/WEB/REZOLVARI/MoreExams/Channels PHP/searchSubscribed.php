<?php
require "connection.php";
$connection = open_connection();

session_start();

$user_name = $_SESSION['username'];

$sql = "select * from channels where subscribers like '%$user_name%'";

if($result = mysqli_query($connection, $sql)){
    $row_counter = 0;

    while($row = mysqli_fetch_assoc($result)){
        //  $channels[$row_counter]['id'] = $row['id'];
        $channels[$row_counter]['ownerid'] = $row['ownerid'];
        $channels[$row_counter]['name'] = $row['name'];
        $channels[$row_counter]['description'] = $row['description'];
        $channels[$row_counter]['subscribers'] = $row['subscribers'];
        $row_counter++;
    }
    //  var_dump($channels);

    echo json_encode($channels);
}
else{
    http_response_code(404);
}