using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using TemplateForExamen.DataAbstractionLayer;
using TemplateForExamen.Models;
namespace TemplateForExamen.Controllers
{
    public class MainController : Controller
    {
        private DAL dal = new DAL();

        // GET: Main
        public ActionResult DashBoard()
        {
            return View();
        }

        public string Test()
        {
            return "It's working";
        }
        [HttpGet]
        [ValidateInput(false)]
        public string GetGames()
        {
            DAL dal = new DAL();
            List<Games> listGames = dal.GetAllGames();
            
            string result = "<table><thead><th>Id</th><th>Title</th><th>Genre</th><th>ReleaseDate</th><th>Platform</th></thead>";

            foreach (Games game in listGames)
            {
                result += "<tr><td>" + game.Id + "</td><td>" + game.Title + "</td><td>" + game.Genre + "</td><td>" + game.ReleaseDate + "</td><td>" + game.Platform + "</td><td></tr>";
            }

            result += "</table>";
            return result;
        }

        [HttpPost]
        public string AddGame()
        {
            string result = "<p>";
            string title = Request.Params["title"];
            string genre = Request.Params["genre"];
            string releaseDate = Request.Params["releaseDate"];
            string platform = Request.Params["platform"];
           

            string message = dal.AddGame(title, genre, releaseDate, platform);
            Console.WriteLine("newGame");

            result += message + "</p>";
            return result;


         
        }
    }
}