<?php

require __DIR__.'/init.php';
$results = $_SESSION['last_results'] ?? [];
$popular = $_SESSION['popular']    ?? null;
$owned = $_SESSION['owned_ids']  ?? [];

?>
<form action="../backend/search.php" method="post">
  <input name="term" placeholder="desc contains...">
  <button>Search</button>
</form>
<a href="../backend/search.php?popular=1">Most Popular</a>

<button> <a href="../frontend/add.php"> Add New Property </a> </button>

<ul>
<?php foreach($results as $r): ?>
  <li>
    <?=htmlspecialchars($r['address'])?> |
    <?=htmlspecialchars($r['description'])?> |  
    <?php if(in_array($r['id'], $owned, true)): ?>
      <a href="../frontend/delete.php?pid=<?= $r['id'] ?>">Delete</a>
    <?php else: ?>
      <a href="../frontend/addExisting.php?pid=<?= $r['id'] ?>">Add</a>
      <?php endif; ?>
  </li>
<?php endforeach;?>
</ul>

<?php if($popular): ?>
  <h3>Most Popular</h3>
  <p><?=htmlspecialchars($popular['address'])?></p>
<?php endif; ?>


<button> <a href="../backend/shared.php"> Shared Properties </a> </button>