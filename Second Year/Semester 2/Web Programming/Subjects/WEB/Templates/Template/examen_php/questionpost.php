<?php
session_start(); 
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
}

if (!isset($_SESSION['username']) || !isset($_SESSION['question'])) {
    header("Location: login.php"); 
    exit();
}

if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit_answer'])) {
    if ($_SESSION['secretAnswer'] === $_POST['answer']) {
        echo "Answer received!";
        header("Location: dashboard.php");
        exit;
    } else {
        session_destroy();
        header("Location: index.php");
        exit;
    }
    
}
?>