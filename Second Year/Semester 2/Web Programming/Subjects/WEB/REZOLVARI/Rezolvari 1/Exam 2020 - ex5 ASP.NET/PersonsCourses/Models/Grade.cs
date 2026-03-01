namespace PersonsCourses.Models
{
    public class Grade
    {
        private int student_id;
        private float grade;

        public Grade(int std_id, float grade)
        {
            this.student_id = std_id;
            this.grade = grade;
        }

        public override string ToString()
        {
            return "{ StudentId: " + student_id + ", Grade: " + grade + " }";
        }

    }
}
