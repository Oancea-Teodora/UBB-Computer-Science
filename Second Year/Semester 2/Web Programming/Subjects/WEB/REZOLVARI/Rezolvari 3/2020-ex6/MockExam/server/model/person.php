<?php
class Person implements JsonSerializable {
    private $id;
    private $name;
    private $age;
    private $gender;

    public function __construct(int $id, string $name, int $age, string $gender) {
        $this->id = $id;
        $this->name = $name;
        $this->age = $age;
        $this->gender = $gender;
    }

    public function getId(): int {
        return $this->id;
    }
 
    public function getName() : string {
        return $this->name;
    }

    public function getAge() : int {
        return $this->age;
    }

    public function getGender() : string {
        return $this->gender;
    }

    public function jsonSerialize() {
        return [
            'id' => $this->id,
            'name' => $this->name,
            'age' => $this->age,
            'gender' => $this->gender,
        ];
    }
}
?>
