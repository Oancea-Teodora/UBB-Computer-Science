<?php

if (session_status() === PHP_SESSION_NONE)
{
    session_start();
}

require __DIR__ . '/db.php';
$pdo = getPDO();
require __DIR__ .'/auth.php';

$name = $_POST['name'] ?? '';
$user = findUserByName($pdo, $name);

if(!$user)
{
    header('Location: ../frontend/login.php?error=1');
    exit;
}

$_SESSION['auth_user'] = $user['id'];
$id = $_SESSION['auth_user'] ?? null;
if($id)
{
    $_SESSION['user_id'] = $id;
    $_SESSION['searches_history'] = [];
    header('Location: ../frontend/search.php');
}

header('Location: ../frontend/search.php');