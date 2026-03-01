using _2024_2.Data;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using static System.Runtime.InteropServices.JavaScript.JSType;
using _2024_2.Models;
using System.Linq;

namespace _2024_2.Controllers
{
    public class DocumentController : Controller
    {
        private readonly AppDbContext _db;
        public DocumentController(AppDbContext db)
        {
            _db = db;
        }
        public IActionResult Index()
        {
            var document = _db.Document.ToList();
            return View(document);
        }


        public IActionResult Search(string title)
        {
            if(string.IsNullOrEmpty(title))
                return View("Index");
            else
            {
                var documents = _db.Document.Where(f => f.title.Contains(title) ).ToList();
                return View("Index", documents);

            }
        }
        public IActionResult Render(int id)
        {

            string text = _db.Document.Where(f => f.ID.Equals(id)).Select(f => f.listOfTemplates).FirstOrDefault();
            string[] ids =  text.Split(',');
            List<string> templates = new List<string>();
            for(int i=0; i<ids.Length;i++)
            {
                var textteamplate = _db.Template.Where(f => f.ID.Equals(int.Parse(ids[i]))).Select(f => f.textContent).FirstOrDefault();
                templates.Add(textteamplate);
            }

            var list = _db.Keyword.ToList();
          //  var dict = list.ToDictionary(row => row.key,row => row.value);
            foreach (var i in list)
            {
                for (int j = 0; j < templates.Count; j++)
                {
                    var content = templates[j];
                    content = content.Replace(i.key, i.value);
                    templates[j] = content;
                }
            }
            
            return View("Render", templates);
        }
    }
}
