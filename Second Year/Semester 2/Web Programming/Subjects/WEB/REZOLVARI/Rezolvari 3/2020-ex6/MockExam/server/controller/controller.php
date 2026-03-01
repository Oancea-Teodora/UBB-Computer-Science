<?php
require_once "../repository/person_repository.php";
require_once "../repository/channel_repository.php";

session_start();

class Controller {
    private $personRepo;

    public function __construct() {
        $this->personRepo = new PersonRepository();
        $this->channelRepo = new ChannelRepository();
    }

    public function serve() {
        if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "login") {
            $this->serveLogin();
        }
        else if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "channels") {
            $this->serveChannels();
        }
        else if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "sub") {
            $this->serveSubscribed();
        }
        else if (isset($_GET) && isset($_GET["func"]) && $_GET["func"] === "new") {
            $this->serveSubscribe();
        }
    }

    private function serveLogin() {
        if (!isset($_GET["user"]))
            return;
        
        $_SESSION["user"] = $_GET["user"];  
    }

    private function serveChannels() {
        if (!isset($_GET["name"]))
            return;
        
        $person = $this->personRepo->selectPersonByName($_GET["name"]);
        echo $this->channelRepo->selectChannelsByOwnerId($person->getId());
    }

    private function serveSubscribed() {
        echo $this->channelRepo->selectAllWithSubscription($_SESSION["user"]);
    }

    private function serveSubscribe() {
        if (!isset($_GET["channel"]))
            return;

        $this->channelRepo->subscribe((int) $_GET["channel"], $_SESSION["user"]);
    }
}

$controller = new Controller();
$controller->serve();
?>
