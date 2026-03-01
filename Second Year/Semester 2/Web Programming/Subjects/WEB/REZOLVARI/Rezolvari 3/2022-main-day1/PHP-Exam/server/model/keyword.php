<?php
class Keyword implements JsonSerializable {
    private $id;
    private $key;
    private $value;

    public function __construct(int $id, string $key, string $value) {
        $this->id = $id;
        $this->key = $key;
        $this->value = $value;
    }

    public function getId(): int {
        return $this->id;
    }
 
    public function getKey() : string {
        return $this->key;
    }

    public function getValue() : string {
        return $this->value;
    }

    public function jsonSerialize() {
        return [
            'id' => $this->id,
            'key' => $this->key,
            'value' => $this->value,
        ];
    }
}
?>
