using System;
using System.Collections.Generic;

#nullable disable

namespace Courses.Models
{
    public partial class Course
    {
        public int Id { get; set; }
        public int Professorid { get; set; }
        public string Coursename { get; set; }
        public string Participants { get; set; }
        public string Grades { get; set; }
    }
}
