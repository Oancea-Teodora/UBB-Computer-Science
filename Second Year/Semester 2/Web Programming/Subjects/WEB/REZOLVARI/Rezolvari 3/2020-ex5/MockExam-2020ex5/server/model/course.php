<?php
class Course implements JsonSerializable {
    private $id;
    private $professorid;
    private $coursename;
    private $participants;
    private $grades;

    public function __construct(int $id, int $professorid, string $coursename, string $participants, string $grades) {
        $this->id = $id;
        $this->professorid = $professorid;
        $this->coursename = $coursename;
        $this->participants = $participants;
        $this->grades = $grades;
    }

    public function getId(): int {
        return $this->id;
    }
 
    public function getProfessorId() : int {
        return $this->professorid;
    }

    public function getCourseName() : string {
        return $this->coursename;
    }

    public function getParticipants() : string {
        return $this->participants;
    }

    public function getGrades() : string {
        return $this->grades;
    }

    public function jsonSerialize() {
        return [
            'id' => $this->id,
            'professorid' => $this->professorid,
            'coursename' => $this->coursename,
            'participants' => $this->participants,
            'grades' => $this->grades,
        ];
    }
}
?>
