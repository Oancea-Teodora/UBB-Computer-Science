using _2024_2.Data;
using _2024_2.Helpers;
using _2024_2.Models;
using Microsoft.AspNetCore.Mvc;

namespace _2024_2.Controllers
{
    public class ProductController : Controller
    {

        private readonly AppDbContext _db;

        public ProductController(AppDbContext db)
        {
            _db = db;
        }
        public IActionResult Index()
        {
            //  var date = HttpContext.Session.GetString("Date");
            //  var city = HttpContext.Session.GetString("City");

            var products = _db.Product.ToList();
            return View(products);
        }

        public bool productCategoryIsInAllOrders(int id)
        {
            var order = _db.Order.OrderByDescending(o => o.id).Take(3);
            var category = _db.Product.Find(id).name.Split('-')[0];
            int nr = 0;
            foreach(var ord in order)
            {
                int orderId = ord.id;
                var orderItems = _db.OrderItem.ToArray();
                foreach (var item in orderItems)
                {
              
                        var categoryOfItem = _db.Product.Find(item.productID).name.Split('-')[0];
                        if (categoryOfItem == category)
                        {
                            nr++;
                        }
                    
                }
            }
            if(nr >= 3)
            {
                return true;
            }
            return false;
        }

        public IActionResult Reserve(int id)
        {
            if (productCategoryIsInAllOrders(id))
            {
                TempData["ShowWarning"] = true;
                TempData["WarningText"] = "That product category is already in your last three orders.";
            }

            if (!HttpContext.Session.Keys.Contains("Price"))
            {
                HttpContext.Session.SetInt32("Price", 0);
            }
            if (!HttpContext.Session.Keys.Contains("Number"))
            {
                HttpContext.Session.SetInt32("Number", 0);
            }
            if (!HttpContext.Session.Keys.Contains("ResIds"))
            {
                HttpContext.Session.SetObjectAsJson("ResIds", new List<int> { });
            }
            int price = HttpContext.Session.GetInt32("Price") ?? 0;
            int number = HttpContext.Session.GetInt32("Number") ?? 0;
            var list = HttpContext.Session.GetObjectFromJson<List<int>>("ResIds");


            var product = _db.Product.Find(id);

            if (product != null && !list.Contains(id))
            {
                price += (int)product.price;
                number++;
                if (list == null)
                {
                    list = new List<int>();
                }
                list.Add(id);
                HttpContext.Session.SetObjectAsJson("ResIds", list);
                HttpContext.Session.SetInt32("Price", price);
                HttpContext.Session.SetInt32("Number", number);
            }
            return RedirectToAction("Index");
        }

        public IActionResult Submit()
        {
            int price = HttpContext.Session.GetInt32("Price") ?? 0;
            var list = HttpContext.Session.GetObjectFromJson<List<int>>("ResIds");
            if (list.Count > 3)
                price -= 10 / 100 * price;
            var dict = new Dictionary<string, object>();
            for (int i = 0; i < list.Count; i++)
            {
                int id = list[i];
                var product = _db.Product.Find(id);

                string category = product.name.Split('-')[0];
                dict[category] = dict.ContainsKey(category) ? (int)dict[category] + 1 : 1;
            }
            for (int i = 0; i < dict.Count; i++)
            {
                if ((int)dict.ElementAt(i).Value > 1)
                {
                    price -= 5 / 100 * price;
                }
            }
            int userId = HttpContext.Session.GetInt32("personID") ?? 0;
            var order = new Order
            {
                userID = userId,
                totalPrice = price
            };

            _db.Order.Add(order);
            _db.SaveChanges();

            foreach (var productId in list)
            {
                var orderItem = new OrderItem
                {
                    orderID = order.id,
                    productID = productId
                };
                _db.OrderItem.Add(orderItem);
            }

            _db.SaveChanges();
            HttpContext.Session.SetObjectAsJson("ResIds", new List<int> { });
            HttpContext.Session.SetObjectAsJson("Price", 0);
            return RedirectToAction(nameof(Index));
        }
    }
}
