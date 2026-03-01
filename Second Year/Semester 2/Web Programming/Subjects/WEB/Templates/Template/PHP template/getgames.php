<?php
    $conn = mysqli_connect('localhost', 'root', '', 'wddb') or die('unable to connect to db');
    session_start();
    
    if($_SERVER['REQUEST_METHOD'] === 'GET') {
      

        $sql = "SELECT * FROM Games";
        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            echo "<table>";
            echo "<tr><th>Title</th><th>Genre</th><th>Release Date</th><th>Platform</th></tr>";

            while ($row = $result->fetch_assoc()) {
                echo "<tr>";
                echo "<td>" . $row["title"] . "</td>";
                echo "<td>" . $row["genre"] . "</td>";
                echo "<td>" . $row["release_date"] . "</td>";
                echo "<td>" . $row["platform"] . "</td>";

                echo "</tr>";
            }
            echo "</table>";
        } else {
            echo "No games found.";
        }
    }
    $conn->close();
?>