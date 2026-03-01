<?php

require __DIR__ . '/init.php';
require __DIR__ . '/properties.php';

$uid = $_SESSION['user_id'];
$pid = (int)($_REQUEST['pid'] ?? 0);

if($pid>0)
{
    insertUserProperty($pdo, $uid, $pid);
    header('Location: ../frontend/search.php');
    exit;
}
