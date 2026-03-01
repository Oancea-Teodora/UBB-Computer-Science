<?php
require 'connection.php';
$connection = open_connection();

session_start();
$username = $_SESSION['username'];

$queryMovies = "select * from movies";
$queryAuthor = "select * from authors where name = '$username'";

$docs = mysqli_query($connection, $queryMovies);
$auth = mysqli_query($connection, $queryAuthor);

$index = 0;
$result = [];

while ($movieRow = mysqli_fetch_array($docs)) {
    $result[$index]['id'] = $movieRow['id'];
    $result[$index]['title'] = $movieRow['title'];
    $result[$index]['duration'] = $movieRow['duration'];
    $index++;
}

$index = 0;
$filtered = [];
$authorRow = mysqli_fetch_array($auth);
$titles = explode(";", $authorRow['movieList']);
foreach ($titles as $name) {
//    echo $name;
//    echo $rowDoc['name'];
    foreach ($result as $movie) {
        if ($name ==$movie['title']) {
            $filtered[$index]['id'] =$movie['id'];
            $filtered[$index]['title'] =$movie['title'];
            $filtered[$index]['duration'] =$movie['duration'];
            $index++;
        }
    }
}

echo json_encode($filtered);
