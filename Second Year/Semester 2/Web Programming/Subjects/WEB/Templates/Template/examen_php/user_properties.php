<?php
session_start(); 
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
}


$db = new mysqli('localhost', 'root', '', 'webexamen');

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

$userId = $_SESSION['userId']; // Assuming user ID is stored in session

// Delete a property
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['delete'])) {
    $propertyId = $_POST['property_id'];
    $deleteQuery = "DELETE FROM UserToProperties WHERE idUser = ? AND idProperty = ?";
    $stmt = $db->prepare($deleteQuery);
    $stmt->bind_param("ii", $userId, $propertyId);
    $stmt->execute();
    echo "Property unlinked successfully.<br>";
    delete_property($db, $userId, $propertyId);
    echo "Deleted Property executed.<br>";
}

function delete_property($db, $userId, $propertyId){

    $checkQuery = "SELECT * FROM UserToProperties WHERE idProperty = ?";
    $stmt = $db->prepare($checkQuery);
    $stmt->bind_param("i", $propertyId);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows == 0) {
        // Property not linked, proceed to link
        $insertQuery = "DELETE FROM Property WHERE id = ?";
        $stmt = $db->prepare($insertQuery);
        $stmt->bind_param("i", $propertyId);
        $stmt->execute();
        echo "Property deleted succesfully too.<br>";
    } else {
        echo "You can't delete this property because you are not the only person owning it.<br>";
    }
}


// Display properties
$query = "SELECT p.id, p.address, p.description FROM Property p INNER JOIN UserToProperties up ON p.id = up.idProperty WHERE up.idUser = ?";
$stmt = $db->prepare($query);
$stmt->bind_param("i", $userId);
$stmt->execute();
$result = $stmt->get_result();

echo "<h2>Your Properties</h2>";
while ($row = $result->fetch_assoc()) {
    echo "ID: " . $row['id'] . ", Address: " . htmlspecialchars($row['address']) . ", Description: " . htmlspecialchars($row['description']) . " ";
    echo "<form method='POST'><input type='hidden' name='property_id' value='" . $row['id'] . "'><button type='submit' name='delete'>Delete</button></form><br>";
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your Properties</title>
</head>
<body>
</body>
</html>
