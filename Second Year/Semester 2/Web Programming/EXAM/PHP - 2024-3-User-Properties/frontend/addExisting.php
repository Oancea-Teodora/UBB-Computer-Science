<?php
require __DIR__.'/init.php';
$pid = $_GET['pid'] ?? null;
if ($pid) {
  // just redirect to backend to link
  header("Location: ../backend/addExisting.php?pid=$pid");
  exit;
}

