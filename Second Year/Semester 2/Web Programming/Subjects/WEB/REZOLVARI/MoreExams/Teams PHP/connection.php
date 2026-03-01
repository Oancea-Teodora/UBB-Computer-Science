<?php
function open_connection() {
    $connection = mysqli_connect("localhost","matei","mamaligacutocana","teams");

    if(!$connection)
    {
        die("Could not connect");
    }
    return $connection;

}


function close_connection(mysqli $connection){
    $connection->close();
}