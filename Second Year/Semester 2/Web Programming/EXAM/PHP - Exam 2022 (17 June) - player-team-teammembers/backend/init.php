<?php

session_start();

require __DIR__ . '/db.php';
$pdo = getPDO();

$currentPage = basename($_SERVER['SCRIPT_NAME']);
$public = ['login.php'];

$isPublicPage = in_array($currentPage, $public);
$isLoggedIn = isset($_SESSION['user_id']);

if(!isset($_SESSION['searches_history'])) {
    $_SESSION['searches_history'] = [];
}

if(!$isPublicPage && !$isLoggedIn)
{
    header('Location: ../frontend/login.php');
    exit;
}
