using System;
using ExamTemplateAsp.Models;
using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;
using MySql.Data.MySqlClient;

namespace ExamTemplateAsp
{
    public class ViewModel
    {
        public IEnumerable<Student> Students { get; set; }
        public int StudentPerPage { get; set; }
        public int CurrentPage { get; set; }

        public int PageCount()
        {
            return Convert.ToInt32(Math.Ceiling(Students.Count() / (double)StudentPerPage));
        }

        public IEnumerable<Student> PaginatedStudents()
        {
            int start = (CurrentPage - 1) * StudentPerPage;

            IEnumerable<Student> result = Students.Skip(start).Take(StudentPerPage);

            return result;
        }
    }
}
