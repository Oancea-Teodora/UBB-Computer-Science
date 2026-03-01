<?php
// api.php
require 'db.php';
$pdo = getPDO();

// Add CORS headers
header('Access-Control-Allow-Origin: *');  // Allow any origin to access this resource
header('Access-Control-Allow-Methods: GET, POST, DELETE, OPTIONS');  // Allow specific methods
header('Access-Control-Allow-Headers: Content-Type, Authorization');  // Allow specific headers
header('Content-Type: application/json');

// api.php
error_log(print_r(getallheaders(), true)); // Log headers to server logs

// Handle pre-flight OPTIONS request for CORS
if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
    // Exit early for OPTIONS request
    exit(0);
}

$action = $_GET['action'] ?? '';

switch ($action) {
    case 'browse':
        // e.g. api.php?action=browse&genre=Rock
        $genre = $_GET['genre'] ?? '';
        $stmt = $pdo->prepare("SELECT id, title FROM multimedia WHERE genre = :genre ORDER BY title");
        $stmt->execute(['genre' => $genre]);
        echo json_encode($stmt->fetchAll());
        break;

    case 'delete':
        // e.g. POST {action:'delete', id:42}
        $data = json_decode(file_get_contents('php://input'), true);
        $stmt = $pdo->prepare("DELETE FROM multimedia WHERE id = :id");
        $stmt->execute(['id' => $data['id']]);
        echo json_encode(['success' => true]);
        break;

    default:
        echo json_encode(['error' => 'Invalid action']);
}
