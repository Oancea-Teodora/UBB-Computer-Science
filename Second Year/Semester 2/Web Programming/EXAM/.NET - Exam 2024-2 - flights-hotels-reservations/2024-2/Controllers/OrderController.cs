using _2024_2.Data;
using _2024_2.Helpers;
using _2024_2.Models;
using Microsoft.AspNetCore.Mvc;

namespace _2024_2.Controllers
{
    public class OrderController : Controller
    {
        private readonly AppDbContext _db;

        public OrderController(AppDbContext db)
        {
            _db = db;
        }
        public IActionResult Index()
        {


            var order = _db.Order.OrderByDescending(o => o.id).Take(3);
             return View(order);
        }
        /*

        public IActionResult Reserve(int id)
        {
            var hotel = _db.Hotels.Find(id);
            if(hotel != null && hotel.availableRooms > 0)
            {
                hotel.availableRooms--;
                var res = new Order { person = HttpContext.Session.GetString("Person"), type = "Hotel", idReservedResource = id };
                _db.Reservations.Add(res);
                _db.SaveChanges();

                var list = HttpContext.Session.GetObjectFromJson<List<int>>("ResIds");
                list.Add(res.id);
                HttpContext.Session.SetObjectAsJson("ResIds", list);
            }
            return RedirectToAction("Index");
        }*/
    }
}
