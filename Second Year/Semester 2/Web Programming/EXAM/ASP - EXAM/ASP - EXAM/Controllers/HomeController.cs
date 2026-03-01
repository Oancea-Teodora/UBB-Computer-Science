using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using _2024_2.Models;
using _2024_2.Data;
using _2024_2.Helpers;

namespace _2024_2.Controllers;

public class HomeController : Controller
{
    private readonly AppDbContext _db;

    public HomeController(AppDbContext db)
    {
        _db = db;
    }

    public IActionResult Index()
    {
        return View();
    }

    [HttpPost]
    public IActionResult Login (string name, string email)
    {
        var t = _db.Customer.FirstOrDefault(f => f.name.Equals(name));
        var e = _db.Customer.FirstOrDefault(f => f.name.Equals(email));
       /* if ( t == null || e == null)
        {
            ModelState.AddModelError(string.Empty, "Wrong user.");
            return View("Index");
        }
       */

        HttpContext.Session.SetString("Name", name);
        HttpContext.Session.SetString("Email", email);

        HttpContext.Session.SetObjectAsJson("ResIds", new List<int>());
        return RedirectToAction("Index", "Feedback");
    }


    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}
