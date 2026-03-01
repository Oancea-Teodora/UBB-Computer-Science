<?php

require "connection.php";
$connection = open_connection();


$id = $_POST['id'];



$delete = "DELETE FROM publishing_house WHERE id=$id";
$connection->query($delete);


$publishingHouses = [];

$sql = "SELECT
    publishing_house.id,
    publishing_house.name,
    publishing_house.url,
    COUNT(book.idPublishingHouse) OVER (PARTITION BY publishing_house.Id) AS count
FROM publishing_house publishing_house
         LEFT JOIN book book
                    ON publishing_house.Id = book.idPublishingHouse";

if($result = mysqli_query($connection, $sql)){
    $row_counter = 0;

    while($row = mysqli_fetch_assoc($result)){

        $publishingHouses[$row_counter]['id'] = $row['id'];
        $publishingHouses[$row_counter]['name'] = $row['name'];
        $publishingHouses[$row_counter]['url'] = $row['url'];
        $publishingHouses[$row_counter]['count'] = $row['count'];
        $row_counter++;
    }

    echo json_encode($publishingHouses);
}
else{
    http_response_code(404);
}