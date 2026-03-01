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

            List<Asset> assets = new List<Asset>();

            try
            {
                conn = new MySql.Data.MySqlClient.MySqlConnection();
                conn.ConnectionString = myConnectionString;
                conn.Open();

                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = conn;
                string id = Session["userId"].ToString();

                cmd.CommandText = "select * from assets where userid=" + id;
                MySqlDataReader myreader = cmd.ExecuteReader();

                while (myreader.Read())
                {
                    Asset asset = new Asset();
                    asset.Id = myreader.GetInt32("id");
                    asset.Name = myreader.GetString("name");
                    asset.Description = myreader.GetString("description");
                    asset.Value = myreader.GetInt32("value");
                    assets.Add(asset);
                }
                myreader.Close();
            }
            catch (MySql.Data.MySqlClient.MySqlException ex)
            {
                Console.Write(ex.Message);
            }

            return View(assets);
        }
    }
}
