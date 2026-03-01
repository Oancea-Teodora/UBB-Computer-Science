using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ASP.NET_Exam.Controllers
{
    public class LoginController : Controller
    {
        public ActionResult Index()
        {
            return View("LoginView");
        }

        public ActionResult Login(string user)
        {
            Session["user"] = user;
            return RedirectToAction("Index", "Main/");
        }
    }
}
