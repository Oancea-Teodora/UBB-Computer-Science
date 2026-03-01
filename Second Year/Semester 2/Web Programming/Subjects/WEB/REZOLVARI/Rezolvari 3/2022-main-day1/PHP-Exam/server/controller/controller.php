<?php
require_once "../model/keyword.php";
require_once "../repository/keyword_repo.php";

session_start();

class Controller {
    private $keywordRepo; 

    public function __construct() {
        $this->keywordRepo = new KeywordRepo();
    }

    public function serve() {
        if (isset($_POST["func"]) && $_POST["func"] === "add") {
            $this->serveAdd();
        }
    }

    private function serveAdd() {
        if (!isset($_POST["key"]) || !isset($_POST["value"])) {
            return;
        }

        $this->keywordRepo->save(new Keyword(-1, $_POST["key"], $_POST["value"]));
    }
}

$controller = new Controller();
$controller->serve();
?>
