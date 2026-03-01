<!-- index.php -->
<?php require 'db.php'; ?>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
  <h1>Browse Multimedia Collection</h1>
  <label for="genre-select">Choose a genre:</label>
  <select id="genre-select">
    <option value="">-- All --</option>
    <?php
      $pdo = getPDO();
      foreach($pdo->query("SELECT DISTINCT genre FROM multimedia") as $row){
        echo "<option>".htmlspecialchars($row['genre'])."</option>";
      }
    ?>
  </select>

  <p id="current-filter">Showing: <strong>All genres</strong></p>
  <ul id="file-list"></ul>

  <script src="js/app.js"></script>
</body>
</html>
