<?php
require 'db.php';
$pdo = getPDO();
$id  = (int)($_GET['id'] ?? 0);

if($_SERVER['REQUEST_METHOD']==='POST'){
  // actually delete
  $stmt = $pdo->prepare("DELETE FROM multimedia WHERE id=:id");
  $stmt->execute(['id'=>$id]);
  header('Location:index.php'); exit;
}

// fetch to show title
$stmt = $pdo->prepare("SELECT title FROM multimedia WHERE id=:id");
$stmt->execute(['id'=>$id]);
$item = $stmt->fetch() ?: ['title'=>'<unknown>'];
?>
<!DOCTYPE html><html><head><link rel="stylesheet" href="css/style.css"></head><body>
  <h1>Confirm Deletion</h1>
  <p>Are you sure you want to delete “<strong><?=htmlspecialchars($item['title'])?></strong>”?</p>
  <form method="post">
    <button>Yes, delete</button>
    <a href="index.php">Cancel</a>
  </form>
</body></html>
