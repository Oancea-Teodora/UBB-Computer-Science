<?php
$host    = '127.0.0.1';
$port    = '3307';        // or '3307' if you changed it
$db      = 'multimedia_app';
$user    = 'appuser';     // or 'root'
$pass    = 'your_pass';   // or '' for root with no password
$charset = 'utf8mb4';

$dsn = "mysql:host=$host;port=$port;dbname=$db;charset=$charset";
$options = [
  PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
  PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
  PDO::ATTR_EMULATE_PREPARES   => false,
];

try {
  $pdo = new PDO($dsn, $user, $pass, $options);
} catch (PDOException $e) {
  exit('Database connection failed.');
}
