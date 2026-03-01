<?php
class Person implements JsonSerializable {
    private $id;
    private $name;
    private $role;

    public function __construct(int $id, int $name, string $role) {
        $this->id = $id;
        $this->name = $name;
        $this->role = $role;
    }

    public function getId(): int {
        return $this->id;
    }

    public function getName() : string {
        return $this->name;
    }

    public function getRole() : string {
        return $this->role;
    }

    public function jsonSerialize() {
        return [
            'id' => $this->id,
            'name' => $this->name,
            'role' => $this->role,
        ];
    }
}
?>
