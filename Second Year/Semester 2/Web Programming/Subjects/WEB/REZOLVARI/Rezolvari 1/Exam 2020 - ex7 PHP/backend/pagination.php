<?php
if (session_status() == PHP_SESSION_NONE) session_start();
if($_SESSION['valid'] == 1) {
    //header('Access-Control-Allow-Origin: *');
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");
    header("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept");

    header("Access-Control-Allow-Credentials: true");

    include 'C:\xampp\htdocs\exam2020ex7\dbconnection.php';

// http://localhost:63343/exam2020ex7/pagination.php?page=2&results=3

    global $conn;

    if (!isset ($_GET['results'])) {
        $results_per_page = 2;
    } else {
        $results_per_page = (int)$_GET['results'];
    }

//find the total number of results stored in the database
    $query = "select * from websites"; //todo - sanitize
    $files = array();
    $result = mysqli_query($conn, $query);

    $number_of_result = mysqli_num_rows($result);

//determine the total number of pages available
    $number_of_page = ceil($number_of_result / $results_per_page);

//determine which page number visitor is currently on
    if (!isset ($_GET['page'])) {
        $page = 1;
    } else {
        $page = $_GET['page'];
    }


//determine the sql LIMIT starting number for the results on the displaying page
    $page_first_result = ($page - 1) * $results_per_page;

//retrieve the selected results from database
    $query = "SELECT * FROM websites  LIMIT " . $page_first_result . ',' . $results_per_page;
    $result = mysqli_query($conn, $query);

//display the retrieved result on the webpage
    while ($row = mysqli_fetch_array($result)) {
        $object = array();
        array_push($object, $row['id']);
        array_push($object, $row['url']);

        array_push($files, $object);
    }

    echo json_encode($files);
}
else{

    echo "this information is private...";
    echo "<form action='destroySession.php' method='POST'>";
    echo " 	 <input type='submit' value='Logout' name='logout' />";
    echo "</form>";

}
?>