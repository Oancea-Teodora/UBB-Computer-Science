<?php
if (session_status() == PHP_SESSION_NONE) session_start();

if($_SESSION['valid'] == 1) {
    include 'C:\xampp\htdocs\exam2020ex7\dbconnection.php';

    //header('Access-Control-Allow-Origin: *');
    //header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");
    header("Access-Control-Allow-Credentials: true");

    global $conn;


//echo "username is: " . $_SESSION['username'];

    $rows = array();
    $selectQuery = mysqli_query($conn, "SELECT * FROM websites");


    while ($row = mysqli_fetch_assoc($selectQuery)) {

        $website = array();
        array_push($website, $row['id']);
        array_push($website, $row['url']);

        // for each website get the number of documents
        //$selectQuery = mysqli_query($conn, "SELECT COUNT(*) FROM `documents` WHERE websiteId = 1;");

        $filterQuery = "SELECT COUNT(*) FROM documents WHERE websiteId = ?";
        $statement = $conn->prepare($filterQuery);
        $statement->bind_param("i", $row['id']);
        $statement->execute();
        $resultSet = $statement->get_result();

        $res = mysqli_fetch_assoc($resultSet);
        //echo $res['COUNT(*)'] . " // " ;
        array_push($website, $res['COUNT(*)']);

        array_push($rows, $website);
    }


    echo json_encode($rows);
}
else{

        echo "this information is private...";
        echo "<form action='destroySession.php' method='POST'>";
        echo " 	 <input type='submit' value='Logout' name='logout' />";
        echo "</form>";

}
?>
