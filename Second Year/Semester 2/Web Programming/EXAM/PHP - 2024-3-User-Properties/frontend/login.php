<?php
$error = isset($_GET['error']);
?>
<form action="../backend/login.php" method="post">
  Name: <input name="name" required>
  <button>Next</button>
</form>
<?php if($error): ?><p style="color:red">Unknown user</p><?php endif; ?>
