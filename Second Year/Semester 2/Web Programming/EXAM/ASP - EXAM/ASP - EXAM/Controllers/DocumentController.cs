/*Write a web application in asp.net for customer feedback that uses the following 3 tables:
table Customer: id(int), name(string), email(string)
table Feedback: id(int), customerId(int), message(text), timestamp(datetime)
table BlockedWords: id(int), pattern(string)

The user should specify his name and email prior to using the app. each customer can upload feedback. upon submission, the app analyzes the 
feedback text using regex patterns from the BlockedWords table. Each pattern may be a simple expression or a regex (eg "brainless"
"\b(fool|idiot|stupid)\"). if more than 3 blocked terms are found, the feedback is flagged (ie a warning message should be displayed to the user
highlighting the matched blocked words).
the user can choose to modify the feedback ot to leave it unchanged. a button for automatically removal of blocked words from
the feesback should be available to the user. then the user can upload the feedback into db

the app should track how many flagged feedback messages this customer has submitted in this hhtp session and this informaation
should always be visible somewhere on the screen.
if the user excedees 2 flagged submissions in this HTTP session, display a warning and restrict further feedback(this means
that the user can still add feedback containing blocked words, but the user this time can only change the feedback or cancel it - the user
can no longer save the feedback into the db)

the user should also be able to see all the feedback of all the customers from the db, nut his own feedback entries should be highlighted
*/
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
            return View();
        }
    }
      
}
