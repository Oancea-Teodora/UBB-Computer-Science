<?php

require __DIR__ . '/init.php';
require __DIR__ . '/auth.php';

$id = $_SESSION['auth_user'] ?? null;
$answer = $_POST['answer'] ?? '';

if($id && checkAnswer($pdo, $id, $answer))
{
    $_SESSION['user_id'] = $id;
    $_SESSION['searches_history'] = [];
    header('Location: ../frontend/search.php');
}
else
{
    header('Location: ../frontend/secret.php?error=1');
    exit;
}