<?php
require __DIR__.'/init.php';
$pid = $_GET['pid'] ?? null;
if ($pid) {
  // just redirect to backend to link
  header("Location: ../backend/add.php?pid=$pid");
  exit;
}
?>
<form action="../backend/add.php" method="post">
  Address: <input name="address" required><br>
  Description:<br>
  <textarea name="description" required></textarea><br>
  <button>Create & Add</button>
</form>
