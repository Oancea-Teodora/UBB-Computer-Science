<?php
require "connection.php";
$connection = open_connection();


$documents = [];

$sql = "SELECT * FROM documents";

if($result = mysqli_query($connection, $sql)){
    $row_counter = 0;

    while($row = mysqli_fetch_assoc($result)){
        //  $channels[$row_counter]['id'] = $row['id'];
        $documents[$row_counter]['name'] = $row['name'];
        $documents[$row_counter]['contents'] = $row['contents'];
        $row_counter++;
    }
    //  var_dump($channels);

    echo json_encode($documents);
}
else{
    http_response_code(404);
}