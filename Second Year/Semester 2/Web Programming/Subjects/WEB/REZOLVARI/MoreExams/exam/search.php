<?php


require "connection.php";
$connection = open_connection();
$word = $_GET["word"];

$words = explode(" ", $word);

$query = "select * from book";

$result = mysqli_query($connection, $query);
$books = [];
$index = 0;
while ($row = mysqli_fetch_assoc($result)) {

    $add = false;
    $matches = 0;
    for ($i = 0, $size = count($words); $i < $size; ++$i) {
        if ($words[$i] == $row['topic1'])
            $matches += 1;
        if ($words[$i] == $row['topic2'])
            $matches += 1;
        if ($words[$i] == $row['topic3'])
            $matches += 1;
        if ($words[$i] == $row['topic4'])
            $matches += 1;
        if ($words[$i] == $row['topic5'])
            $matches += 1;

    }

    if ($matches == 3) {
        $books[$index]['id'] = $row['id'];
        $books[$index]['idPublishingHouse'] = $row['idPublishingHouse'];
        $books[$index]['name'] = $row['name'];
        $books[$index]['topic1'] = $row['topic1'];
        $books[$index]['topic2'] = $row['topic2'];
        $books[$index]['topic3'] = $row['topic3'];
        $books[$index]['topic4'] = $row['topic4'];
        $books[$index]['topic5'] = $row['topic5'];
        $index += 1;
    }
}


echo json_encode($books);