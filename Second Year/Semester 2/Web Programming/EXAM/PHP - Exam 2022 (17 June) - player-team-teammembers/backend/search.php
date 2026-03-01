<?php
require 'init.php';
require 'properties.php';

header('Content-Type: application/json');

$q = $_GET['q'] ?? '';

if (strlen($q) > 0) {
    $results = getSearchedPlayers($pdo, '%' . $q . '%');
    echo $results;
} else {
    $results = getPlayers($pdo);
    echo $results;
}
?>