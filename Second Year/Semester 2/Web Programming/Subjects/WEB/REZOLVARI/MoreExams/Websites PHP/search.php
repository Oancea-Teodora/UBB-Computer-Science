<?php


require "connection.php";
$connection = open_connection();
$word = $_GET["word"];

$words = explode(" ", $word);

$query = "select * from documents";

$result = mysqli_query($connection, $query);
$document = [];
$index = 0;
while ($row = mysqli_fetch_assoc($result)) {

    $add = false;
    $matches = 0;
    for ($i = 0, $size = count($words); $i < $size; ++$i) {
        if ($words[$i] == $row['keyword1'])
            $matches += 1;
        if ($words[$i] == $row['keyword2'])
            $matches += 1;
        if ($words[$i] == $row['keyword3'])
            $matches += 1;
        if ($words[$i] == $row['keyword4'])
            $matches += 1;
        if ($words[$i] == $row['keyword5'])
            $matches += 1;
        if ($matches == 3) {
            $document[$index]['id'] = $row['id'];
            $document[$index]['idWebsite'] = $row['idWebsite'];
            $document[$index]['name'] = $row['name'];
            $document[$index]['keyword1'] = $row['keyword1'];
            $document[$index]['keyword2'] = $row['keyword2'];
            $document[$index]['keyword3'] = $row['keyword3'];
            $document[$index]['keyword4'] = $row['keyword4'];
            $document[$index]['keyword5'] = $row['keyword5'];
            $index += 1;
        }
    }
}


echo json_encode($document);