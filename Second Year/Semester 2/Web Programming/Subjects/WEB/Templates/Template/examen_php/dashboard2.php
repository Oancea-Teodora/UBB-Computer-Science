<?php
session_start(); 
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
}
$db = new mysqli('localhost', 'root', '', 'webexamen');

if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

// function to track and display most searched property
if (isset($_POST['show_most_searched']) && isset($_SESSION['property_counts'])) {
    $most_searched_id = array_keys($_SESSION['property_counts'], max($_SESSION['property_counts']))[0];
    $query = "SELECT address, description FROM Property WHERE id = ?";
    $stmt = $db->prepare($query);
    $stmt->bind_param("i", $most_searched_id);
    $stmt->execute();
    $result = $stmt->get_result()->fetch_assoc();
    echo "Most searched property: Address: " . htmlspecialchars($result['address']) . ", Description: " . htmlspecialchars($result['description']) . "<br><br>";
}

// handle searches
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['search'])) {
    $search = $db->real_escape_string($_POST['search']);
    $query = "SELECT * FROM Property WHERE description LIKE ?";
    $stmt = $db->prepare($query);
    $search_term = "%" . $search . "%";
    $stmt->bind_param("s", $search_term);
    $stmt->execute();
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        echo "Address: " . htmlspecialchars($row['address']) . "<br>Description: " . htmlspecialchars($row['description']) . "<br>";
        if (!isset($_SESSION['property_counts'][$row['id']])) {
            $_SESSION['property_counts'][$row['id']] = 0;
        }
        $_SESSION['property_counts'][$row['id']]++;
    }
}

// add a new property
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['add_new_property'])) {
    $address = $_POST['new_address'];
    $description = $_POST['new_description'];
    check_for_add($db, $address, $description);
    $querySelect = "SELECT id FROM Property WHERE address = ? and description = ?";
    $stmt = $db->prepare($querySelect);
    $stmt->bind_param("ss", $address,$description);
    $stmt->execute();
    $result = $stmt->get_result()->fetch_assoc();
    $new_property_id = $result['id'];
    link_property_to_user($db, $_SESSION['userId'], $new_property_id);
    echo "New property added and linked to your profile.<br>";
}

// link an existing property
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['link_property'])) {
    $property_id = $_POST['property_id'];
    link_property_to_user($db, $_SESSION['userId'], $property_id);
    echo "Existing property linked to your profile.<br>";
}

function check_for_add($db, $address, $description){
    $checkQuery = "SELECT * FROM Property WHERE address = ? AND description = ?";
    $stmt = $db->prepare($checkQuery);
    $stmt->bind_param("ss", $address, $description);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows == 0) {
        // not existent -> make existent
        $insertQuery = "INSERT INTO Property (address, description) VALUES (?, ?)";
        $stmt = $db->prepare($insertQuery);
        $stmt->bind_param("ss", $address, $description);
        $stmt->execute();
        echo "Property added.<br>";
    } else {
        echo "Property existent.<br>";
    }
}

function link_property_to_user($db, $userId, $propertyId) {
    // Check if the property is already linked
    $checkQuery = "SELECT * FROM UserToProperties WHERE idUser = ? AND idProperty = ?";
    $stmt = $db->prepare($checkQuery);
    $stmt->bind_param("ii", $userId, $propertyId);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows == 0) {
        // not linked -> link
        $insertQuery = "INSERT INTO UserToProperties (idUser, idProperty) VALUES (?, ?)";
        $stmt = $db->prepare($insertQuery);
        $stmt->bind_param("ii", $userId, $propertyId);
        $stmt->execute();
        echo "Property linked successfully.<br>";
    } else {
        echo "You already own this property.<br>";
    }

}

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Property Search and Management</title>
</head>
<body>
<h2>Search Properties</h2>
<form method="POST">
    <input type="text" name="search" placeholder="Search properties...">
    <button type="submit">Search</button>
</form>

<form method="POST">
    <button name="show_most_searched" type="submit">Show Most Searched Property</button>
</form>

<h2>Add New Property</h2>
<form method="POST">
    <input type="text" name="new_address" placeholder="Address" required>
    <input type="text" name="new_description" placeholder="Description" required>
    <button name="add_new_property" type="submit">Add New Property</button>
</form>

<input type="button" value="User Properties" class="propertiesbutton" id="userProperties" 
onClick="window.location = 'http://localhost/Examen_php/user_properties.php'" />
<input type="button" value="Shared Properties" class="sharedbutton" id="userProperties" 
onClick="window.location = 'http://localhost/Examen_php/shared_properties.php'" />

<input type="button" value="Logout" class="logout" id="logout" 
onClick="window.location = 'http://localhost/Examen_php/logout.php'" />
</body>
</html>
