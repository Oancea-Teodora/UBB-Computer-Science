<?php
header("Content-Type: application/json");
$origin = "http://localhost:4200"; // Adjust this to match your Angular app's URL

header("Access-Control-Allow-Origin: $origin");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With");

$con = mysqli_connect('localhost', 'root', '', 'wddb') or die('unable to connect to db');
session_start();
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['login'])) {
    $username = mysqli_real_escape_string($con, $_POST['username']);
    $password = mysqli_real_escape_string($con, $_POST['password']); 

    $select = mysqli_query($con, "SELECT * FROM users WHERE userName='$username' AND userPass='$password'");
    $row = mysqli_fetch_array($select);

    if ($row) {
        $_SESSION['username'] = $row['Username'];
        $_SESSION['userId'] = $row['Id'];
        $response = [
            'success' => true,
            'userId' => $row['Id'],
            'username' => $row['Username'],
            'message' => 'Login successful!'
        ];
        header("Location: main.php");
        // echo json_encode($response);
    } else {
        $response = ['success' => false, 'message' => 'Invalid username or password.'];
        // echo json_encode($response);
        http_response_code(401);
    }
    exit;
}
?>