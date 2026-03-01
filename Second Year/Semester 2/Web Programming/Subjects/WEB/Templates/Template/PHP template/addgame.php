<?php
    $conn = new mysqli('localhost', 'root', '', 'wddb');

    if($conn->connect_error) {
        die('unable to connect to db');
    }

    if($_SERVER['REQUEST_METHOD'] === 'POST') {
        if(isset($_POST['title'], $_POST['genre'], $_POST['release_date'], $_POST['platform'])) {
            $title = $_POST['title'];
            $genre = $_POST['genre'];
            $release_date = $_POST['release_date'];
            $platform = $_POST['platform'];
        

           
                $stmt = $conn->prepare("INSERT INTO Games (title, genre, release_date, platform) VALUES (?, ?, ?, ?)");
                $stmt->bind_param("ssss", $title, $genre, $release_date, $platform);

                if ($stmt->execute()) {
                    echo "New game added successfully";
                } else {
                    echo "Error: " . $stmt->error;
                }
                $stmt->close();
            
        } else {
            echo "All parameters are required.";
        }
    }
    $conn->close();
?>