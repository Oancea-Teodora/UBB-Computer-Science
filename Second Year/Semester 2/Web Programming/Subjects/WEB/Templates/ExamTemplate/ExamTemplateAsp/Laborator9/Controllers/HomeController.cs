using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using MySql.Data.MySqlClient;
using ExamTemplateAsp.Models;
using System.Web.UI.WebControls;
using System.Web.UI;
using PagedList.Mvc;
using PagedList;

namespace ExamTemplateAsp.Controllers
{
    public class HomeController : Controller
    {
        [HttpGet]
        public ActionResult Index()
        {
            MySql.Data.MySqlClient.MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "datasource=localhost;port=3306;username=root;password=;database=exam";

            List<Student> students = new List<Student>();

            try
            {
                conn = new MySql.Data.MySqlClient.MySqlConnection();
                conn.ConnectionString = myConnectionString;
                conn.Open();

                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = conn;
                cmd.CommandText = "select * from students ";
                MySqlDataReader myreader = cmd.ExecuteReader();

                while (myreader.Read())
                {
                    Student student = new Student();
                    student.Id = myreader.GetInt32("id");
                    student.Name = myreader.GetString("name");
                    student.Grade = myreader.GetInt32("grade");
                    students.Add(student);
                }
                myreader.Close();
            }
            catch (MySql.Data.MySqlClient.MySqlException ex)
            {
                Console.Write(ex.Message);
            }

            return View(students);
        }

        public string GetFiltered()
        {
            string student_name = Request.Params["student_name"];
            string result = "<table><tr><th>Id&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th><th>Name&nbsp;&nbsp;&nbsp;</th><th>Grade&nbsp;</th><th>Update&nbsp;</th><th>Delete&nbsp;</th></tr></table>";
            result += "<script>function deleteSubmit(form) { if (confirm('Are you sure you want to delete this student? ')) { form.submit(); return true; } else { return false; } }</script>";

            MySql.Data.MySqlClient.MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "datasource=localhost;port=3306;username=root;password=;database=exam";

            List<Student> students = new List<Student>();

            try
            {
                conn = new MySql.Data.MySqlClient.MySqlConnection();
                conn.ConnectionString = myConnectionString;
                conn.Open();

                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = conn;
                cmd.CommandText = "select * from students where name='" + student_name + "'";
                MySqlDataReader myreader = cmd.ExecuteReader();

                while (myreader.Read())
                {
                    Student student = new Student();
                    student.Id = myreader.GetInt32("id");
                    student.Name = myreader.GetString("name");
                    student.Grade = myreader.GetInt32("grade");
                    students.Add(student);
                    result += "<form method='post' class='main-form'>";
                    result += "<input type = 'hidden' id = 'sid' name = 'sid' value ='" + student.Id + " '/>" + student.Id;
                    result += "<input type = 'text' id = 'sname' name = 'sname' value='" + student.Name + " '/>";
                    result += "<input type = 'text' id = 'sgrade' name = 'sgrade' value='" + student.Grade + " '/>";
                    result += "<input type='submit' name='update' value='Update' formaction='/Home/Update' />";
                    result += "<input type='submit' name='delete' value='Delete' onClick='return deleteSubmit(this.form);' formaction = '/Home/Delete' />";
                    result += "</form>";
                }
                myreader.Close();
            }
            catch (MySql.Data.MySqlClient.MySqlException ex)
            {
                Console.Write(ex.Message);
            }

            return result;
        }

        public ActionResult Update()
        {
            string id = Request.Params["sid"];
            string name = Request.Params["sname"];
            string grade = Request.Params["sgrade"];
            int integer_grade = -1;

            try
            {
                if (name == "" || grade == "")
                {
                    TempData["error"] = "Please fill all the fields before update!";
                    return RedirectToAction("Index", "Home");
                }

                integer_grade = Int32.Parse(grade);
                if (integer_grade < 1 || integer_grade > 10)
                {
                    TempData["error"] = "Grade must be in [1, 10] interval!";
                    return RedirectToAction("Index", "Home");
                }
            }
            catch
            {
                TempData["error"] = "Grade must be a number!";
                return RedirectToAction("Index", "Home");
            }

            MySql.Data.MySqlClient.MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "datasource=localhost;port=3306;username=root;password=;database=exam";

            try
            {
                conn = new MySql.Data.MySqlClient.MySqlConnection();
                conn.ConnectionString = myConnectionString;
                conn.Open();

                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = conn;
                cmd.CommandText = "update students set name='" + name + "', grade='" + grade + "' where id='" + id + "'";
                cmd.ExecuteNonQuery();

            }
            catch (MySql.Data.MySqlClient.MySqlException ex)
            {
                Console.Write(ex.Message);
            }

            return RedirectToAction("Index", "Home");
        }

        public ActionResult Delete()
        {
            string id = Request.Params["sid"];

            MySql.Data.MySqlClient.MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "datasource=localhost;port=3306;username=root;password=;database=exam";

            try
            {
                conn = new MySql.Data.MySqlClient.MySqlConnection();
                conn.ConnectionString = myConnectionString;
                conn.Open();

                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = conn;
                cmd.CommandText = "delete from students where id=" + id;
                cmd.ExecuteNonQuery();

            }
            catch (MySql.Data.MySqlClient.MySqlException ex)
            {
                Console.Write(ex.Message);
            }

            return RedirectToAction("Index", "Home");
        }
    }
}
