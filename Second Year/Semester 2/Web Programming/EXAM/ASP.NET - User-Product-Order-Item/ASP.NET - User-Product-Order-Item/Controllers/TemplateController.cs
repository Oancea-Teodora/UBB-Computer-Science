using Microsoft.AspNetCore.Mvc;

namespace _2024_2.Controllers
{
    public class TemplateController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
