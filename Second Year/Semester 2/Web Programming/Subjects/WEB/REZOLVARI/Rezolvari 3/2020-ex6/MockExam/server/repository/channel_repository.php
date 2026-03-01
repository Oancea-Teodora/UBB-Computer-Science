<?php
require_once "../model/channel.php";

class ChannelRepository {
    private $hostname = "localhost:3307";
    private $username = "root";
    private $password = "";
    private $database = "web-ex6";
    private $tableName = "Channels";
    private $connection = NULL;

    public function __construct() {
        $this->connect();
    }

    public function __destruct() {
        $this->connection->close();
    }

    private function connect() {
        $this->connection = new mysqli($this->hostname, $this->username, $this->password, $this->database);
        
        // check connection
        if ($this->connection->connect_error) {
            die("Connection failed: " . $this->connection->connect_error);
        }
    }

    public function selectChannelsByOwnerId(int $ownerId) {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "` WHERE `ownerid` = '" . $ownerId . "';";

        $answerArray = array();

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        if ($result->num_rows > 0) {
            // output data from each row
            while($row = $result->fetch_assoc()) {
                $channel = new Channel((int) $row["id"], (int) $row["ownerid"], $row["name"], $row["description"], $row["subscribers"]);
                array_push($answerArray, json_encode($channel));
            }
        }
        else {
            return $answerArray;
        }        

        return json_encode($answerArray);
    }

    public function selectAllWithSubscription(string $subscriber) {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "`;";
        $answerArray = array();

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        if ($result->num_rows > 0) {
            // output data from each row
            while($row = $result->fetch_assoc()) {
                $subscr = explode(",", $row["subscribers"]);

                foreach($subscr as $crt) {
                    $sb = explode(":", $crt);
                    if (count($sb) != 2) {
                        continue;
                    }
                    
                    if ($sb[0] == $subscriber) {
                        $channel = new Channel((int) $row["id"], (int) $row["ownerid"], $row["name"], $row["description"], $row["subscribers"]);
                        array_push($answerArray, json_encode($channel));
                    }
                }
            }
        }
        else {
            return $answerArray;
        }        

        return json_encode($answerArray);
    }

    public function subscribe(int $channelId, string $subscriber) {
        $sqlQuery = "SELECT * FROM `" . $this->tableName . "` WHERE id = '" . $channelId . "';";
        $answerArray = array();
        $updateDate = false;
        $subscribers = "";
        $newSub = "";

        $result = $this->connection->query($sqlQuery);
        if (!$result) {
            return;
        }
        
        if ($result->num_rows > 0) {
            // output data from each row
            $row = $result->fetch_assoc();
            $subscr = explode(",", $row["subscribers"]);
            $subscribers = $row["subscribers"];

            foreach($subscr as $crt) {
                $sb = explode(":", $crt);
                if (count($sb) != 2) {
                    continue;
                }
                
                if ($sb[0] == $subscriber) {
                    $updateDate = true;
                    $newSub = $newSub . $sb[0] . ":" . date("Y/m/d") . ",";
                }
                else {
                    $newSub = $newSub . $crt . ",";
                }
            }
        }
        else {
            return null;
        }

        $sql = "UPDATE `" . $this->tableName . "` SET `subscribers` = '" . $subscribers . $subscriber . ":" . date("Y/m/d") . ",' WHERE `id` = '" . (string) $channelId . "'";
        if ($updateDate) {
            $sql = "UPDATE `" . $this->tableName . "` SET `subscribers` = '" . $newSub . "' WHERE `id` = '" . (string) $channelId . "'";
        }

        $this->connection->query($sql);
    }
}
?>
