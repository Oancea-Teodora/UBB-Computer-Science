<?php
require 'connection.php';
$connection = open_connection();
session_start();
$username = $_SESSION['username'];

$documentQuery = "select * from documents";
$authorQuery = "select * from authors";

$docs = mysqli_query($connection, $documentQuery);
$auth = mysqli_query($connection, $authorQuery);

$index = 0;
$result = [];

while ($documentRow = mysqli_fetch_array($docs)) {
    $result[$index]['id'] = $documentRow['id'];
    $result[$index]['name'] = $documentRow['name'];
    $result[$index]['contents'] = $documentRow['contents'];
    $index++;
}

$index = 0;
$authors = [];
while ($authorRow = mysqli_fetch_array($auth)) {
    $authors[$index]['documentList'] = $authorRow['documentList'];
    $index++;
}

$mostPopularName = "";
$maxAuthors = 0;

foreach ($result as $document) {

    $count = 0;
    foreach ($authors as $author) {
        if (str_contains($author['documentList'], $document['name'])) {
            $count++;
        }
    }

    if ($count > $maxAuthors) {
        $maxAuthors = $count;
        $mostPopularName = $document['name'];
    }
}

echo $mostPopularName;