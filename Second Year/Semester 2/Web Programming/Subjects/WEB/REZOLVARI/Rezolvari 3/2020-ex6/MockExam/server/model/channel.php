<?php
class Channel implements JsonSerializable {
    private $id;
    private $ownerId;
    private $name;
    private $description;
    private $subscribers;

    public function __construct(int $id, int $ownerId, string $name, string $description, string $subscribers) {
        $this->id = $id;
        $this->ownerId = $ownerId;
        $this->name = $name;
        $this->description = $description;
        $this->subscribers = $subscribers;
    }

    public function getId(): int {
        return $this->id;
    }
 
    public function getOwnerId() : int {
        return $this->ownerId;
    }

    public function getName() : string {
        return $this->name;
    }

    public function getDescription() : string {
        return $this->description;
    }

    public function getSubscribers() : string {
        return $this->subscribers;
    }

    public function jsonSerialize() {
        return [
            'id' => $this->id,
            'ownerId' => $this->ownerId,
            'name' => $this->name,
            'description' => $this->description,
            'subscribers' => $this->subscribers,
        ];
    }
}
?>
