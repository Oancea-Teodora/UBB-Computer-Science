<?php

require "connection.php";
$connection = open_connection();

session_start();


$name = $_POST["name"];
$contents = $_POST["contents"];


$insert = $connection->prepare("INSERT INTO documents (name, contents) VALUES (?,?)");
$insert->bind_param("ss", $name, $contents);
$insert->execute();


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
