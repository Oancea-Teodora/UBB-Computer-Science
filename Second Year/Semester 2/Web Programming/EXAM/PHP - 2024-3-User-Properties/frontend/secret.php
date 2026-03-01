<?php
require '../backend/init.php';
$id = $_SESSION['auth_user'] ?? header('Location: login.php');
$q  = $pdo->prepare("SELECT secretQuestion FROM Users WHERE id=?");
$q->execute([$id]);
$question = $q->fetchColumn();
$error = isset($_GET['error']);
?>
<p><?=htmlspecialchars($question)?></p>
<form action="../backend/verify.php" method="post">
  <input name="answer" required>
  <button>Submit</button>
</form>
<?php if($error): ?><p style="color:red">Wrong answer</p><?php endif; ?>
