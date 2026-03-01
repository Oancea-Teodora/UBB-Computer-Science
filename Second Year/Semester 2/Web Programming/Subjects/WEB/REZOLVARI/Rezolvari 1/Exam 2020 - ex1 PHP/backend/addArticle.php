<?php

// send user through url
// SELECT * FROM `assets` WHERE userid = 1;

include 'C:\xampp\htdocs\exam2020ex1\backend\dbconnection.php';

//header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Origin: http://localhost:4200");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");


if (session_status() == PHP_SESSION_NONE) session_start();
if ($_SESSION['valid'] == 1) {

    global $conn;

    $json = file_get_contents('php://input');
    $article = json_decode($json, true);
    $user = $_POST['username'];
    $journalid = $_POST['journal'];
    $summary = $_POST['summary'];

    //echo $journalid;

//echo "username is: " . $_SESSION['username'];

    $rows = array();
    // // SELECT * FROM `assets` WHERE userid = 1;
    $selectQuery = "SELECT * FROM journal where name = ?";
    //$filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
    $statement = $conn->prepare($selectQuery);
    $statement->bind_param("s", $journalid);
    $statement->execute();
    $resultSet = $statement->get_result();

    while ($row = mysqli_fetch_assoc($resultSet)) {

        $document = array();
        array_push($document, $row['id']);
        array_push($document, $row['name']);

        array_push($rows, $document);
    }

    if(sizeof($rows) == 0){
        // add new journal
        $addQuery = $conn->prepare("INSERT INTO journal(name) VALUES (?)");
        $addQuery->bind_param("s", $journalid);
        $addQuery->execute();
    }
    $date = rand(0, 10000);

    // select the id of the journal
    $selectQuery = "SELECT * FROM journal where name = ?";
    //$filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
    $statement = $conn->prepare($selectQuery);
    $statement->bind_param("s", $journalid);
    $statement->execute();
    $resultSet = $statement->get_result();

    $row = mysqli_fetch_assoc($resultSet);
    $jid = $row['id'];

    // insert the article
    // INSERT INTO `articles` (`id`, `user`, `journalid`, `summary`, `date`) VALUES (NULL, 'seb', '1', 'ugug', '6');
    $addQuery = $conn->prepare("INSERT INTO articles(user, journalid, summary, date) VALUES (?, ?, ?, ?)");
    $addQuery->bind_param("sisi", $user, $jid, $summary, $date);
    $addQuery->execute();

    echo json_encode("ok");

} else {
    echo "this information is private...";
    echo "<form action='destroySession.php' method='POST'>";
    echo " 	 <input type='submit' value='Logout' name='logout' />";
    echo "</form>";
}


