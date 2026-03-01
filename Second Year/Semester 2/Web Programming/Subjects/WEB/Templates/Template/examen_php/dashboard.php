<?php
session_start(); 
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
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
<form action="dashboardphp.php" method="POST">
    <input type="text" name="search" placeholder="Search properties...">
    <button type="submit">Search</button>
</form>

<form action="dashboardphp.php" method="POST">
    <button name="show_most_searched" type="submit">Show Most Searched Property</button>
</form>

<h2>Add New Property</h2>
<form action="dashboardphp.php" method="POST">
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
