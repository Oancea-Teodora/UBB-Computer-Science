<?php

function findUserByName($pdo, $name)
{
    $stmt = $pdo->prepare("SELECT * FROM Users WHERE name=?");
    $stmt->execute([$name]);
    return $stmt->fetch(PDO::FETCH_ASSOC);
}

function checkAnswer($pdo, $name, $answer)
{
    $stmt = $pdo->prepare("SELECT secretAnswer FROM Users WHERE id=?");
    $stmt->execute([$name]);
    return $stmt->fetchColumn() === $answer;
}