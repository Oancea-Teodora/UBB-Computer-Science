<?php
session_start(); 
if (!isset($_SESSION['username'])) {
     header("Location: index.php");
}

// if (!isset($_SESSION['username']) || !isset($_SESSION['question'])) {
//     header("Location: login.php"); 
//     exit();
// }

// if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit_answer'])) {
//     if ($_SESSION['secretAnswer'] === $_POST['answer']) {
//         echo "Answer received!";
//         header("Location: dashboard.php");
//         exit;
//     } else {
//         session_destroy();
//         header("Location: index.php");
//         exit;
//     }
    
// }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Answer Security Question</title>
</head>
<body>
    <h2>Answer Your Security Question</h2>
    <form action="questionpost.php" method="POST">
        <p><?php echo htmlspecialchars($_SESSION['question']); ?></p>
        <input type="text" name="answer" required>
        <button type="submit" name="submit_answer">Submit Answer</button>
    </form>
</body>
</html>
