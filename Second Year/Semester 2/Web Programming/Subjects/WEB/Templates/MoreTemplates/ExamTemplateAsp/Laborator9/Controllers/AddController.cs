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
    public class AddController : Controller
    {
        public ActionResult Index()
        {
            return View ();
        }

        public ActionResult Add()
        {
            string name = Request.Params["sname"];
            string grade = Request.Params["sgrade"];
            int integer_grade = -1;

            try
            {
                if (name == "" || grade == "")
                {
                    TempData["error"] = "Please fill all the fields before update!";
                    return RedirectToAction("Index", "Add");
                }

                integer_grade = Int32.Parse(grade);
                if (integer_grade < 1 || integer_grade > 10)
                {
                    TempData["error"] = "Grade must be in [1, 10] interval!";
                    return RedirectToAction("Index", "Add");
                }
            }
            catch
            {
                TempData["error"] = "Grade must be a number!";
                return RedirectToAction("Index", "Add");
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
                cmd.CommandText = "insert into students (name, grade) values('" + name + "','" + grade + "')";
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
