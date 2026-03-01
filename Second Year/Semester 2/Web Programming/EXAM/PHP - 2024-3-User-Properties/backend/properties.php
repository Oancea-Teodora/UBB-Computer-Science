<?php

function searchProperties($pdo, $term)
{
    try {
        $stmt = $pdo->prepare("SELECT * FROM Properties WHERE description LIKE ?");
        $stmt->execute(["%{$term}%"]);
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);
        error_log("Search results: " . count($results) . " found for term: " . $term);
        return $results;
    } catch(PDOException $e) {
        error_log("Search error: " . $e->getMessage());
        return [];
    }
}

function logSearch($id)
{
    array_push($_SESSION['searches_history'], $id);
}

function getPopular($pdo)
{
    if(empty($_SESSION['searches_history']))
    {
        return null;
    }
    $counts = array_count_values($_SESSION['searches_history']);
    arsort($counts);
    $bestpopular = key($counts);
    $stmt = $pdo->prepare("SELECT * FROM Properties WHERE id=?");
    $stmt->execute([$bestpopular]);
    return $stmt->fetch(PDO::FETCH_ASSOC);
}

function insertUserProperty($pdo, $uid, $pid)
{
    $stmt = $pdo->prepare("INSERT IGNORE INTO UserToProperties (userId, propertyId) VALUES (?,?)");
    $stmt->execute([$uid, $pid]);
}

function createProperty($pdo, $addr, $descr)
{
    $stmt = $pdo->prepare("INSERT INTO Properties (address, description) VALUES (?, ?)");
    $stmt->execute([$addr, $descr]);
    return $pdo->lastInsertId();
}

function getAllProperties($pdo)
{
    try {
        $stmt = $pdo->prepare("SELECT * FROM Properties ORDER BY id");
        $stmt->execute();
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    } catch(PDOException $e) {
        error_log("Get all properties error: " . $e->getMessage());
        return [];
    }
}

function deleteUserProperty($pdo, $uid, $pid)
{
    // First check how many users own this property
    $countStmt = $pdo->prepare("SELECT COUNT(*) as count FROM UserToProperties WHERE propertyId = ?");
    $countStmt->execute([$pid]);
    $count = $countStmt->fetch(PDO::FETCH_ASSOC)['count'];
    
    // Delete the user-property relationship
    $stmt = $pdo->prepare("DELETE FROM UserToProperties WHERE userId=? AND propertyId=?");
    $stmt->execute([$uid, $pid]);
    
    // If this was the only user, delete the property itself
    if ($count == 1) {
        $deletePropertyStmt = $pdo->prepare("DELETE FROM Properties WHERE id=?");
        $deletePropertyStmt->execute([$pid]);
    }
}

function getShared($pdo)
{
    $stmt = $pdo->prepare("SELECT p.*, COUNT(*) AS owners
        FROM Properties p
        JOIN UserToProperties u ON u.propertyId = p.id
       GROUP BY p.id
      HAVING COUNT(*) > 1");
      $stmt->execute();
      return $stmt->fetchAll(PDO::FETCH_ASSOC);
}