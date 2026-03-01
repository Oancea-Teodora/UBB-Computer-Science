using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using TemplateForExamen.Models;
using TemplateForExamen.DataAbstractionLayer;

namespace TemplateForExamen.Controllers
{
    public class LoginController : Controller
    {
        private DAL dal = new DAL();

        // GET: Login
        public ActionResult Index()
        {
            return View();
        }
        [HttpGet]
        public ActionResult Login()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Login(string username, string password)
        {
            User user = dal.GetUserByUserAndPassword(username, password);
            if (user != null)
            {
                Session["UserId"] = user.Id;
                Session["Username"] = user.Username;
                return RedirectToAction("DashBoard", "Main");
            }
            ModelState.AddModelError("", "Invalid login attempt.");
            return View();
        }

        public ActionResult Logout()
        {
            Session.Clear();
            return RedirectToAction("Index", "Home");
        }
    }
}