<?php
// add.php (simplified)
require 'db.php';
$errors = [];
if($_SERVER['REQUEST_METHOD']==='POST'){
  // validation
  if(empty($_POST['title'])) $errors[] = 'Title is required';
  // … other checks …

  if(!$errors){
    $pdo = getPDO();
    $stmt = $pdo->prepare(
      "INSERT INTO multimedia (title,format_type,genre,file_path)
       VALUES (:title,:fmt,:genre,:path)"
    );
    $stmt->execute([
      'title'=>$_POST['title'],
      'fmt'=>$_POST['format_type'],
      'genre'=>$_POST['genre'],
      'path'=>$_POST['file_path']
    ]);
    header('Location: index.php'); exit;
  }
}
?>
<!DOCTYPE html>
<html><head><link rel="stylesheet" href="css/style.css"></head><body>
  <h1>Add New Multimedia File</h1>
  <?php if($errors): ?>
    <div class="errors">
      <ul><?php foreach($errors as $e) echo "<li>$e</li>"; ?></ul>
    </div>
  <?php endif; ?>
  <form method="post">
    <label>Title: <input name="title" required></label><br>
    <label>Format: <input name="format_type" required></label><br>
    <label>Genre:  <input name="genre" required></label><br>
    <label>Path:   <input name="file_path" required></label><br>
    <button>Add</button>
    <a href="index.php">Cancel</a>
  </form>
</body></html>
