using Microsoft.AspNetCore.Mvc;
using PersonsCourses.Data_Abstraction_Layer;

namespace PersonsCourses.Controllers
{
    public class MainController : Controller
    {
        private DAL dal;
        public IActionResult Index()
        {
            return View();
        }

        public MainController()
        {
            this.dal = new DAL();
        }

        public string GetParticipants(string course)
        {
            return dal.GetParticipants(course); 
            
        }

        public List<string> GetCourses(string studentName)
        {
            return dal.GetCourses(studentName);
        }

        private string UpdateGrade(string grades, string participants, string studentName, int grade)
        {
            string[] stud = participants.Split(',');
            string[] grd = grades.Split(",");
            for (int i = 0; i < stud.Length; i++)
            {
                if (stud[i].Trim() == studentName)
                    grd[i] = grade.ToString();
            }
            string computeUpdatedGrade = "";
            foreach (String g in grd)
            {
                computeUpdatedGrade += g + ", ";
            }
            computeUpdatedGrade = computeUpdatedGrade.Substring(0, computeUpdatedGrade.Length - 2);
            return computeUpdatedGrade;
        }


        public string GiveGrade(string course, string studentName, int grade)
        {
            string pg=this.dal.GetParticipantsAndGrades(course);
            string[] result = pg.Split("+");
            string updatedString= this.UpdateGrade(result[0],result[1],studentName,grade);

            DAL dal1 = new DAL();
            if (result[1].Contains(studentName)) //student already registered to course, just update grade
            {
                dal1.updateGrade(course, updatedString);
            }
            else //add both student+grade
            {
                dal1.AddStudent(course, result[0] + ", " + grade, result[1] + ", " + studentName);
            }
            
            return "ok";
        }

        public List<string> GetMyCourses()
        {
            string id = HttpContext.Session.GetString("id");
            return dal.GetMyCourses(int.Parse(id));
        }
    }
}
