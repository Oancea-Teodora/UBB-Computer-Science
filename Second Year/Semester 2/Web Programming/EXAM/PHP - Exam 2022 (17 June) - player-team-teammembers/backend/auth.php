<?php

function findUserByName($pdo, $name)
{
    $stmt = $pdo->prepare("SELECT * FROM Player WHERE name=?");
    $stmt->execute([$name]);
    return $stmt->fetch(PDO::FETCH_ASSOC);
}

