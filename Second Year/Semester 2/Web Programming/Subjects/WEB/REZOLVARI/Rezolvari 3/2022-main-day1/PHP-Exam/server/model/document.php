<?php
class Template implements JsonSerializable {
    private $id;
    private $title;
    private $templates;

    public function __construct(int $id, string $title, string $templates) {
        $this->id = $id;
        $this->title = $title;
        $this->templates = $templates;
    }

    public function getId(): int {
        return $this->id;
    }
 
    public function getTitle() : string {
        return $this->title;
    }

    public function getTemplates() : string {
        return $this->templates;
    }

    public function jsonSerialize() {
        return [
            'id' => $this->id,
            'title' => $this->title,
            'templates' => $this->templates,
        ];
    }
}
?>
