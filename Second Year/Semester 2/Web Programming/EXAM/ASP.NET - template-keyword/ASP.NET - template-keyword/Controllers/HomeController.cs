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
    public IActionResult Begin (string person, string date, string city)
    {
        if(string.IsNullOrEmpty(person) || string.IsNullOrEmpty(date) || string.IsNullOrEmpty(city))
        {
            ModelState.AddModelError(string.Empty, "All fields are required.");
            return View("Index");
        }
       
        HttpContext.Session.SetString("Person", person);
        HttpContext.Session.SetString("Date", date);
        HttpContext.Session.SetString("City", city);

        HttpContext.Session.SetObjectAsJson("ResIds", new List<int>());
        return RedirectToAction("Index", "Flights");
    }


    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}
