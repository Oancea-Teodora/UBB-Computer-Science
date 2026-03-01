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
using Newtonsoft.Json;

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
            //Get data from client
            string json = Request.Params["json"];

            //Data is received as an array, so we must deserialize it
            dynamic stuff = JsonConvert.DeserializeObject(json);

            //Prepare the connection
            MySql.Data.MySqlClient.MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "datasource=localhost;port=3306;username=root;password=;database=exam";


            //Parse an array of jsons
            foreach (String s in stuff)
            {
                Asset asset = JsonConvert.DeserializeObject<Asset>(s);
                int user_id = Int32.Parse(Session["userId"].ToString());
                String name = asset.Name;
                String description = asset.Description;
                int value = asset.Value;

                int integer_value = -1;

                try
                {
                    if (string.IsNullOrEmpty(asset.Id.ToString()) || string.IsNullOrEmpty(asset.Name) || string.IsNullOrEmpty(asset.Description) || string.IsNullOrEmpty(asset.Value.ToString()))
                    {
                        TempData["error"] = "Please fill all the fields before adding!";
                        return RedirectToAction("Index", "Add");
                    }

                    integer_value = Int32.Parse(asset.Value.ToString());
                }
                catch
                {
                    TempData["error"] = "Value must be a number!";
                    return RedirectToAction("Index", "Add");
                }

                //Do the modifications in the database
                try
                {
                    conn = new MySql.Data.MySqlClient.MySqlConnection();
                    conn.ConnectionString = myConnectionString;
                    conn.Open();

                    MySqlCommand cmd = new MySqlCommand();
                    cmd.Connection = conn;
                    cmd.CommandText = "insert into assets (userid, name, description, value) values('" + user_id + "','" + name + "','" + description + "','" + value + "')";
                    cmd.ExecuteNonQuery();
                    conn.Close();

                }
                catch (MySql.Data.MySqlClient.MySqlException ex)
                {
                    Console.Write(ex.Message);
                }
            }
            return RedirectToAction("Index", "Home");
        }
    }
}
