<?php
require "connection.php";
$connection = open_connection();


$teams = [];

$sql = "SELECT * FROM teams";

if($result = mysqli_query($connection, $sql)){
    $row_counter = 0;

    while($row = mysqli_fetch_assoc($result)){
        //  $channels[$row_counter]['id'] = $row['id'];
        $teams[$row_counter]['id'] = $row['id'];
        $teams[$row_counter]['captainId'] = $row['captainId'];
        $teams[$row_counter]['name'] = $row['name'];
        $teams[$row_counter]['description'] = $row['description'];
        $teams[$row_counter]['members'] = $row['members'];
        $row_counter++;
    }
    //  var_dump($channels);

    echo json_encode($teams);
}
else{
    http_response_code(404);
}