<?php
require "connection.php";
$connection = open_connection();


$documents = [];

$sql = "SELECT * FROM documents";

if($result = mysqli_query($connection, $sql)){
    $row_counter = 0;

    while($row = mysqli_fetch_assoc($result)){
        //  $channels[$row_counter]['id'] = $row['id'];
        $documents[$row_counter]['id'] = $row['id'];
        $documents[$row_counter]['idWebsite'] = $row['idWebsite'];
        $documents[$row_counter]['name'] = $row['name'];
        $documents[$row_counter]['keyword1'] = $row['keyword1'];
        $documents[$row_counter]['keyword2'] = $row['keyword2'];
        $documents[$row_counter]['keyword3'] = $row['keyword3'];
        $documents[$row_counter]['keyword4'] = $row['keyword4'];
        $documents[$row_counter]['keyword5'] = $row['keyword5'];
        $row_counter++;
    }
    //  var_dump($channels);

    echo json_encode($documents);
}
else{
    http_response_code(404);
}