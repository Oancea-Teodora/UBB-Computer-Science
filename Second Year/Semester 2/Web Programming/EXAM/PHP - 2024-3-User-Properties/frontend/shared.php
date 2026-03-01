<?php

require __DIR__.'/init.php';

$shared = $_SESSION['shared'] ?? [];
?>
<h2>Properties with >1 owner</h2>
<ul>
<?php foreach($shared as $p): ?>
  <li>
    <?=htmlspecialchars($p['address'])?> (<?=$p['owners']?> owners)
  </li>

<?php endforeach;?>
</ul>
