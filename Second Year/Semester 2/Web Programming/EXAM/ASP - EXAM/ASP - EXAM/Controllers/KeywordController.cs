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

            return View();
        }

    }
}
