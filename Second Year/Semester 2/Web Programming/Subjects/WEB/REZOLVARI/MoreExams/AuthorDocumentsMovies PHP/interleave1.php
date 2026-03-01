<?php
require 'connection.php';
$connection = open_connection();

session_start();
$username = $_SESSION['username'];

$queryDocuments = "select * from documents";
$queryAuthor = "select * from authors where name = '$username'";

$documents = mysqli_query($connection, $queryDocuments);
$author = mysqli_query($connection, $queryAuthor);

$index = 0;
$result = [];

while ($documentRow = mysqli_fetch_array($documents)) {
    $result[$index]['id'] = $documentRow['id'];
    $result[$index]['name'] = $documentRow['name'];
    $result[$index]['contents'] = $documentRow['contents'];
    $index++;
}

$index = 0;
$filtered = [];
$authorRow = mysqli_fetch_array($author);
$names = explode(";", $authorRow['documentList']);
foreach ($names as $name) {
//    echo $name;
//    echo $rowDoc['name'];
    foreach ($result as $doc) {
        if ($name ==$doc['name']) {
            $filtered[$index]['id'] =$doc['id'];
            $filtered[$index]['name'] =$doc['name'];
            $filtered[$index]['contents'] =$doc['contents'];
            $index++;
        }
    }
}

echo json_encode($filtered);

