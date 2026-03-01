using _2024_2.Data;
using _2024_2.Models;
using Microsoft.AspNetCore.Mvc;
using static Microsoft.Extensions.Logging.EventSource.LoggingEventSource;

namespace _2024_2.Controllers
{
    public class FeedbackController : Controller
    {
        private readonly AppDbContext _db;
        public FeedbackController(AppDbContext db)
        {
            _db = db;
        }
        public IActionResult Index()
        {
            var feedback = _db.Feedback.ToArray();
            return View(feedback);
        }

        public IActionResult Add(string text)
        {
            if (string.IsNullOrEmpty(text))
            {
                ModelState.AddModelError(string.Empty, "All fields are required.");
                return View("Index");
            }
            var customer = _db.Customer.Where(f => f.name.Equals(HttpContext.Session.GetString("Person"))).FirstOrDefault();
          
            var res = new Feedback {customerId =(int)customer.id ,  text = text, timestamp = DateTime.Now };
            _db.Feedback.Add(res);
            _db.SaveChanges();

            return RedirectToAction("Index"); 
        }
    }
}
