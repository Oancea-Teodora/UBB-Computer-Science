<?php

require 'connection.php';
session_start();

if(isset($_SESSION['username']))
{
    $connection = open_connection();
    $user_name = $_SESSION["username"];
    $channelName = $_POST["channelName"];

    $stmt = $connection->prepare("SELECT * FROM channels WHERE name = ?");
    $stmt->bind_param("s", $channelName);
    $stmt->execute();

    $result = $stmt->get_result();
    $row = $result->fetch_assoc();

    $subscribers = $row["subscribers"];
    $new_subscribers = "";
    if(strpos($subscribers, $user_name) == false){
        //daca nu e subscribed userul la channel
        $new_subscribers = $subscribers." ".$user_name."-22.06.2021;";
    }
    else {
        $subscribersList = explode(";", $subscribers);
        foreach($subscribersList as $subscription) {
            if (strpos($subscription, $user_name) == false)
            {
                $new_subscribers = $new_subscribers.$subscription.";";
            }
            else {
                $new_subscribers = $new_subscribers." ".$user_name."-21.06.2021";
            }
        }
    }

    $update = $connection->prepare("UPDATE channels SET subscribers = ? WHERE name = ?");
    $update->bind_param("ss", $new_subscribers, $channelName);
    $update->execute();



    $sql = "select * from channels where subscribers like '%$user_name%'";

    if($result = mysqli_query($connection, $sql)){
        $row_counter = 0;

        while($row = mysqli_fetch_assoc($result)){
            //  $channels[$row_counter]['id'] = $row['id'];
            $channels[$row_counter]['ownerid'] = $row['ownerid'];
            $channels[$row_counter]['name'] = $row['name'];
            $channels[$row_counter]['description'] = $row['description'];
            $channels[$row_counter]['subscribers'] = $row['subscribers'];
            $row_counter++;
        }
        //  var_dump($channels);

        echo json_encode($channels);
    }
}

else
{
    echo "You didn't specify your name.";
}

?>
