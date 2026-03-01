<?php

require __DIR__ .'/init.php';
require __DIR__ .'/auth.php';

$name = $_POST['name'] ?? '';
$user = findUserByName($pdo, $name);

if(!$user)
{
    header('Location: ../frontend/login.php?error=1');
    exit;
}

$_SESSION['auth_user'] = $user['id'];
header('Location: ../frontend/secret.php');