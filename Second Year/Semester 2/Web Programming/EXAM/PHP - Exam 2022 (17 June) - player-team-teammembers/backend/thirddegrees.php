<?php
require 'init.php';
require 'properties.php';

header('Content-Type: application/json');

$results = getThirdDegree($pdo);
echo $results;

?> 