<?php
// SELECT * FROM `documents` where keyword1 = "vettel";
if (session_status() == PHP_SESSION_NONE) session_start();
if($_SESSION['valid'] == 1) {

    include 'C:\xampp\htdocs\exam2020ex7\dbconnection.php';

//header('Access-Control-Allow-Origin: *');
    header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, X-Requested-With");
    header("Access-Control-Allow-Origin: http://localhost:4200");
    header("Access-Control-Allow-Credentials: true");
    global $conn;

// get keywords and put them in an array
//if (str_contains('How are you', 'are')) {
//    echo 'true';
//}

// SELECT keyword1, keyword2, keyword3, keyword4, keyword5 FROM `documents`;
//$string = array('vettel', 'ferrari', 'rand');
    $string = $_GET['array'];
    $array = explode(" ", $string);
//echo json_encode($array);
    $rows = array();
    $selectQuery = mysqli_query($conn, "SELECT * FROM documents");

    while ($row = mysqli_fetch_assoc($selectQuery)) {

        $document = array();
        array_push($document, $row['keyword1']);
        array_push($document, $row['keyword2']);
        array_push($document, $row['keyword3']);
        array_push($document, $row['keyword4']);
        array_push($document, $row['keyword5']);

        //echo json_encode($document);
        $count = 0;
        foreach ($array as $item) {
            //echo $item;
            if (str_contains(json_encode($document), $item)) {
                //echo "true " . $item;
                $count = $count + 1;
            }
        }

        //echo "<br>" . $count . " for " . json_encode($document);

        if ($count == 3) {
            array_push($document, $row['id']);
            array_push($document, $row['websiteId']);
            array_push($document, $row['name']);
            array_push($rows, $document);
        }
    }

    echo json_encode($rows);
}
?>