<?php
// send user through url
// SELECT * FROM `assets` WHERE userid = 1;

include 'C:\xampp\htdocs\exam2020ex2\backend\dbconnection.php';

//header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");


if (session_status() == PHP_SESSION_NONE) session_start();
var_dump($_SESSION);
if (isset($_SESSION['valid']) && $_SESSION['valid'] == 1) {

    global $conn;

//echo "username is: " . $_SESSION['username'];

    $username = $_GET['name'];
    $channelId = $_GET['cid'];
    $rows = array();
    // // SELECT * FROM `assets` WHERE userid = 1;
    $selectQuery = "SELECT * FROM channels where id = ?";
    //$filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
    $statement = $conn->prepare($selectQuery);
    $statement->bind_param("i", $channelId);
    $statement->execute();
    $resultSet = $statement->get_result();

    $row = mysqli_fetch_assoc($resultSet);

    //echo $row['subscribers'];
    $result = array_values(json_decode($row['subscribers'], true));

    $flag = 0;
    foreach($result as $item){
        //echo array_values($item)[1] . " ;;;; ";
        if(array_values($item)[1] == $username) {
            //echo "<br>true<br>";
            $flag = 1;
        }
    }

    if($flag == 0) {
        //echo "add";
        $res = array("date" => rand(0,1000), "name"=>$username);
        // array('image' => $link, 'folder' => $category);
        //array_push($res, $username);
        //array_push($res, 34);
        array_push($result, $res);
        //echo "<br>" . json_encode($result);
    }
    else {
        foreach($result as &$item){
            //echo array_values($item)[1] . " ;;;; ";
            if(array_values($item)[1] == $username) {
                //array_values($item)[0] = rand(0, 1000);
                 $item['date'] = rand(0, 1000);
                //echo "<br>true<br>";
                //$flag = 1;
            }
            //echo "updated: " . $item['date'] . "<br>";
        }
        //echo "<br>" . json_encode($result);

    }
    $subs = json_encode($result);

    // update it in db
    $updateQuery = $conn->prepare("UPDATE channels SET subscribers=? WHERE id = ?");
    $updateQuery->bind_param("si", $subs, $channelId);
    $updateQuery->execute();

//        $document = array();
//        array_push($document, $row['id']);
//        array_push($document, $row['ownerId']);
//        array_push($document, $row['name']);
//        array_push($document, $row['description']);
//        array_push($document, $row['subscribers']);
//
//        array_push($rows, $document);
//
//
//
//    echo json_encode($rows);
}
else {
    echo "this information is private...";
    echo "<form action='destroySession.php' method='POST'>";
    echo " 	 <input type='submit' value='Logout' name='logout' />";
    echo "</form>";
}


