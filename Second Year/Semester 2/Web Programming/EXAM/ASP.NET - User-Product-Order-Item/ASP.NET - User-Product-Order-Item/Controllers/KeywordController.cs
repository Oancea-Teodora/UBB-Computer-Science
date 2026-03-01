using Microsoft.AspNetCore.Mvc;
using _2024_2.Data;
using _2024_2.Helpers;
using _2024_2.Models;

namespace _2024_2.Controllers
{
    public class KeywordController : Controller
    {
        private readonly AppDbContext _db;
        public KeywordController(AppDbContext db)
        {
            _db = db;
        }
        public IActionResult Index()
        {
            var keyword = _db.Keyword;
            return View(keyword);
        }

        public IActionResult Add(string key, string value)
        {
            if(string.IsNullOrEmpty(key) || string.IsNullOrEmpty(value))
            {
                ModelState.AddModelError(string.Empty, "All fields are required.");
                return View("Index");
            }
            var res = new Keyword { key = key, value = value };
            _db.Keyword.Add(res);
            _db.SaveChanges();

            return RedirectToAction("Index");

        }
           
    
    }
}
