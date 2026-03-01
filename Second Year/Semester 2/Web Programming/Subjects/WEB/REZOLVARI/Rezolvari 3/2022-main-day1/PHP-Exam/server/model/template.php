<?php
class Template implements JsonSerializable {
    private $id;
    private $name;
    private $textContent;

    public function __construct(int $id, string $name, string $textContent) {
        $this->id = $id;
        $this->name = $name;
        $this->textContent = $textContent;
    }

    public function getId(): int {
        return $this->id;
    }
 
    public function getName() : string {
        return $this->name;
    }

    public function getTextContent() : string {
        return $this->textContent;
    }

    public function jsonSerialize() {
        return [
            'id' => $this->id,
            'name' => $this->name,
            'textContent' => $this->textContent,
        ];
    }
}
?>
