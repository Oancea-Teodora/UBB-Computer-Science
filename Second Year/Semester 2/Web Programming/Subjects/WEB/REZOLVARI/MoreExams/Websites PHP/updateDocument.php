<?php

require "connection.php";
$connection = open_connection();

$id = $_POST['id'];
$keyword1 = $_POST["keyword1"];
$keyword2 = $_POST["keyword2"];
$keyword3 = $_POST["keyword3"];
$keyword4 = $_POST["keyword4"];
$keyword5 = $_POST["keyword5"];



$update = "UPDATE documents SET keyword1='$keyword1', keyword2='$keyword2', keyword3='$keyword3', keyword4='$keyword4', keyword5='$keyword5' WHERE id=$id";
$connection->query($update);


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