<?php
require "connection.php";
$connection = open_connection();


$websites = [];

$sql = "SELECT
    website.url,
    COUNT(document.idWebsite) OVER (PARTITION BY website.Id) AS count
FROM websites website
         INNER JOIN documents document
                    ON website.Id = document.idWebsite";

if($result = mysqli_query($connection, $sql)){
    $row_counter = 0;

    while($row = mysqli_fetch_assoc($result)){
        //  $channels[$row_counter]['id'] = $row['id'];
        $websites[$row_counter]['url'] = $row['url'];
        $websites[$row_counter]['count'] = $row['count'];
        $row_counter++;
    }
    //  var_dump($channels);

    echo json_encode($websites);
}
else{
    http_response_code(404);
}