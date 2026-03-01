using Microsoft.AspNetCore.Mvc;
using PersonsCourses.Data_Abstraction_Layer;
using PersonsCourses.Models;
using System.Diagnostics;

namespace PersonsCourses.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        public IActionResult Index()
        {
            return View();
            //return Redirect("/Main");
        }

        public int VerifyUser(string name)
        {
            DAL dal = new DAL();
            int id= dal.Authenticate(name);
            HttpContext.Session.SetString("id", id.ToString());
            return id;
        }

        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}