<?php

require __DIR__ . '/init.php';
require __DIR__ . '/properties.php';

$results = [];

if(!empty($_POST['term']))
{
    $results = searchProperties($pdo, $_POST['term']);
    foreach($results as $r)
    {
        logSearch($r['id']);
    }
}
else
{
    // Show all properties if no search term
    $results = getAllProperties($pdo);
}

if(isset($_GET['popular']))
{
    $_SESSION['popular'] = getPopular($pdo);
}

$_SESSION['last_results'] = $results;

//////////////////////////
$st = $pdo->prepare("
  SELECT propertyId
    FROM UserToProperties
   WHERE userId = ?
");
$st->execute([$_SESSION['user_id']]);
$_SESSION['owned_ids'] = $st->fetchAll(PDO::FETCH_COLUMN);

header('Location: ../frontend/search.php');