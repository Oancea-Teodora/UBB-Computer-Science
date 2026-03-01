<?php
session_start(); 
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
}
$db = new mysqli('localhost', 'root', '', 'webexamen');

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

// Display properties with more than one owner
$query = "SELECT p.id, p.address, p.description, COUNT(up.idUser) as owner_count FROM Property p INNER JOIN UserToProperties up ON p.id = up.idProperty GROUP BY p.id HAVING owner_count > 1";
$result = $db->query($query);

echo "<h2>Properties with Multiple Owners</h2>";
while ($row = $result->fetch_assoc()) {
    echo "ID: " . $row['id'] . ", Address: " . htmlspecialchars($row['address']) . ", Description: " . htmlspecialchars($row['description']) . ", Owners: " . $row['owner_count'] . "<br>";
}
?>