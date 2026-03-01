<?php
require __DIR__.'/init.php';
require __DIR__.'/properties.php';

// 1) Delete the link in the database
deleteUserProperty($pdo, $_SESSION['user_id'], (int)$_GET['pid']);

// 2) Pull out the old results from session
$old = $_SESSION['last_results'] ?? [];

// 3) Filter out the just‐deleted property
$new = array_filter($old, fn($r) => $r['id'] !== (int)$_GET['pid']);

// 4) Save the filtered list back into session
$_SESSION['last_results'] = array_values($new);



// 5) Redirect back to the search page
header('Location: ../frontend/search.php');
exit;
