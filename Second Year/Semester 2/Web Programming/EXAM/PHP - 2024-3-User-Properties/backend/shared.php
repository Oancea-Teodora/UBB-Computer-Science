<?php
require __DIR__.'/init.php';
require __DIR__.'/properties.php';

$_SESSION['shared'] = getShared($pdo);
header('Location: ../frontend/shared.php');
