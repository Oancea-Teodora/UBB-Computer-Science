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

if ($_SERVER['REQUEST_METHOD'] === 'POST') 
{
    $newPid = createProperty($pdo, $_POST['address'], $_POST['description']);
    insertUserProperty($pdo, $uid, $newPid);
  }
  
// 3) Re‐fetch your owned IDs so the session is up-to-date
$ownedStmt = $pdo->prepare("
    SELECT propertyId
      FROM UserToProperties
     WHERE userId = ?
");
$ownedStmt->execute([$uid]);
$_SESSION['owned_ids'] = $ownedStmt->fetchAll(PDO::FETCH_COLUMN);

// 4) Redirect back to the search page and stop execution
header('Location: ../frontend/search.php');