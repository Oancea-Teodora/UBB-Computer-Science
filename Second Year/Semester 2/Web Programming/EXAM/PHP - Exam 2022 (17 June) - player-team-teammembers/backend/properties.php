<?php

function getSearchedPlayers($pdo, $name)
{
    $stmt = $pdo->prepare("SELECT name FROM Player WHERE name LIKE ?");
    $stmt->execute([$name]);
    $searchedPlayers = [];
    while($fetch1 = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $searchedPlayers[] = $fetch1['name'];
    }
    return json_encode($searchedPlayers);
}

function getPlayers($pdo)
{
    $stmt = $pdo->prepare("SELECT name FROM Player");
    $stmt->execute();
    $searchedPlayers = [];
    while($fetch1 = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $searchedPlayers[] = $fetch1['name'];
    }
    return json_encode($searchedPlayers);
}

function getFirstDegree($pdo)
{
    $id = $_SESSION['user_id'];
    $stmt = $pdo->prepare("SELECT name FROM  Player p JOIN TeamMembers t ON p.ID=t.IDplayer1 WHERE t.IDplayer2=?");
    $stmt->execute([$id]);

    $stmt2 = $pdo->prepare("SELECT name FROM Player p JOIN TeamMembers t ON p.ID=t.IDplayer2 WHERE t.IDplayer1=?");
    $stmt2->execute([$id]);

    $firstDegree = [];
    while($fetch1 = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $firstDegree[] = $fetch1['name'];
    }
    while($fetch1 = $stmt2->fetch(PDO::FETCH_ASSOC)) {
        $firstDegree[] = $fetch1['name'];
    }
    $_SESSION['firstdegree'] = $firstDegree;

    return json_encode($firstDegree);
}


/**
 * Return JSON‐encoded array of names exactly 2° away from the current user.
 */
function getSecondDegree(PDO $pdo): string
{
    $me = (int)($_SESSION['user_id'] ?? 0);
    if ($me === 0) {
        return json_encode([]);
    }

    // 1) Prepare the “neighbor” query once
    $nbrQ = $pdo->prepare("
      SELECT IDplayer1 AS pid FROM TeamMembers WHERE IDplayer2 = ?
      UNION
      SELECT IDplayer2         FROM TeamMembers WHERE IDplayer1 = ?
    ");

    // 2) Fetch your 1° IDs
    $first = [];
    if ($nbrQ->execute([$me, $me])) {
        $first = $nbrQ->fetchAll(PDO::FETCH_COLUMN);
    }

    if (empty($first)) {
        return json_encode([]);
    }

    // 3) Build a visited set = { you } ∪ 1°
    $visited   = array_fill_keys(array_merge([$me], $first), true);
    $secondIds = [];

    // 4) For each 1°, pull *their* neighbors
    foreach ($first as $fid) {
        if (! $nbrQ->execute([$fid, $fid])) {
            continue;
        }
        $cands = $nbrQ->fetchAll(PDO::FETCH_COLUMN);
        foreach ($cands as $nid) {
            if (!isset($visited[$nid])) {
                $visited[$nid]     = true;
                $secondIds[$nid]   = $nid;
            }
        }
    }

    if (empty($secondIds)) {
        return json_encode([]);
    }

    // 5) Turn those IDs into sorted names
    $ph  = implode(',', array_fill(0, count($secondIds), '?'));
    $st  = $pdo->prepare("SELECT name FROM Player WHERE ID IN ($ph) ORDER BY name");
    if ($st->execute(array_keys($secondIds))) {
        $names = $st->fetchAll(PDO::FETCH_COLUMN);
        return json_encode($names);
    }

    return json_encode([]);
}

/**
 * Return JSON‐encoded array of names exactly 3° away from the current user.
 */
function getThirdDegree(PDO $pdo): string
{
    $me = (int)($_SESSION['user_id'] ?? 0);
    if ($me === 0) {
        return json_encode([]);
    }

    // reuse the same neighbor‐of‐X query
    $nbrQ = $pdo->prepare("
      SELECT IDplayer1 AS pid FROM TeamMembers WHERE IDplayer2 = ?
      UNION
      SELECT IDplayer2         FROM TeamMembers WHERE IDplayer1 = ?
    ");

    // 1°:
    $first = [];
    if ($nbrQ->execute([$me, $me])) {
        $first = $nbrQ->fetchAll(PDO::FETCH_COLUMN);
    }

    // 2°:
    $visited   = array_fill_keys(array_merge([$me], $first), true);
    $secondIds = [];
    foreach ($first as $fid) {
        if (! $nbrQ->execute([$fid, $fid])) continue;
        foreach ($nbrQ->fetchAll(PDO::FETCH_COLUMN) as $nid) {
            if (!isset($visited[$nid])) {
                $visited[$nid]    = true;
                $secondIds[$nid]  = $nid;
            }
        }
    }

    if (empty($secondIds)) {
        return json_encode([]);
    }

    // 3°:
    // now visited = { you, 1°, 2° }
    $visited = array_fill_keys(array_merge([$me], $first, array_keys($secondIds)), true);
    $thirdIds = [];
    foreach (array_keys($secondIds) as $sid) {
        if (! $nbrQ->execute([$sid, $sid])) continue;
        foreach ($nbrQ->fetchAll(PDO::FETCH_COLUMN) as $nid) {
            if (!isset($visited[$nid])) {
                $visited[$nid]   = true;
                $thirdIds[$nid]  = $nid;
            }
        }
    }

    if (empty($thirdIds)) {
        return json_encode([]);
    }

    // fetch their names
    $ph = implode(',', array_fill(0, count($thirdIds), '?'));
    $st = $pdo->prepare("SELECT name FROM Player WHERE ID IN ($ph) ORDER BY name");
    if ($st->execute(array_keys($thirdIds))) {
        $names = $st->fetchAll(PDO::FETCH_COLUMN);
        return json_encode($names);
    }

    return json_encode([]);
}



