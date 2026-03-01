using _2024_2.Data;
using _2024_2.Helpers;
using _2024_2.Data;
using _2024_2.Helpers;
using _2024_2.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Text.RegularExpressions;

namespace _2024_2.Controllers
{
    public class HomeController : Controller
    {
        private readonly AppDbContext _context;

        public HomeController(AppDbContext context)
        {
            _context = context;
        }

        public IActionResult Index()
        {
            var sessionInfo = HttpContext.Session.GetObjectFromJson<SessionInfo>("SessionInfo");

            if (sessionInfo == null)
            {
                return View("CustomerRegistration");
            }

            var feedbacks = _context.Feedbacks
                .Include(f => f.Customer)
                .OrderByDescending(f => f.Timestamp)
                .Select(f => new FeedbackDisplayViewModel
                {
                    Id = f.Id,
                    CustomerName = f.Customer.Name,
                    CustomerEmail = f.Customer.Email,
                    Message = f.Message,
                    Timestamp = f.Timestamp,
                    IsCurrentUser = f.CustomerId == sessionInfo.CustomerId
                })
                .ToList();

            ViewBag.SessionInfo = sessionInfo;
            return View("FeedbackList", feedbacks);
        }

        [HttpPost]
        public IActionResult RegisterCustomer(CustomerRegistrationViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View("CustomerRegistration", model);
            }

            var existingCustomer = _context.Customers.FirstOrDefault(c => c.Email == model.Email);
            Customer customer;

            if (existingCustomer != null)
            {
                customer = existingCustomer;
            }
            else
            {
                customer = new Customer
                {
                    Name = model.Name,
                    Email = model.Email
                };
                _context.Customers.Add(customer);
                _context.SaveChanges();
            }

            var sessionInfo = new SessionInfo
            {
                CustomerId = customer.Id,
                CustomerName = customer.Name,
                CustomerEmail = customer.Email,
                FlaggedCount = 0,
                IsRestricted = false
            };

            HttpContext.Session.SetObjectAsJson("SessionInfo", sessionInfo);

            return RedirectToAction("Index");
        }

        public IActionResult SubmitFeedback()
        {
            var sessionInfo = HttpContext.Session.GetObjectFromJson<SessionInfo>("SessionInfo");
            if (sessionInfo == null)
            {
                return RedirectToAction("Index");
            }

            ViewBag.SessionInfo = sessionInfo;
            return View(new FeedbackViewModel());
        }

        [HttpPost]
        public IActionResult SubmitFeedback(FeedbackViewModel model)
        {
            var sessionInfo = HttpContext.Session.GetObjectFromJson<SessionInfo>("SessionInfo");
            if (sessionInfo == null)
            {
                return RedirectToAction("Index");
            }

            if (string.IsNullOrWhiteSpace(model.Message))
            {
                ModelState.AddModelError("Message", "Feedback message is required.");
                ViewBag.SessionInfo = sessionInfo;
                return View(model);
            }

            // Check for blocked words
            var blockedWords = _context.BlockedWords.ToList();
            var foundBlockedWords = new List<string>();

            foreach (var blockedWord in blockedWords)
            {
                try
                {
                    var regex = new Regex(blockedWord.Pattern, RegexOptions.IgnoreCase);
                    if (regex.IsMatch(model.Message))
                    {
                        foundBlockedWords.Add(blockedWord.Pattern);
                    }
                }
                catch (ArgumentException)
                {
                    // If regex is invalid, treat as simple string match
                    if (model.Message.Contains(blockedWord.Pattern, StringComparison.OrdinalIgnoreCase))
                    {
                        foundBlockedWords.Add(blockedWord.Pattern);
                    }
                }
            }

            model.BlockedWordsFound = foundBlockedWords;
            model.IsFlagged = foundBlockedWords.Count > 3;
            model.CanSave = !sessionInfo.IsRestricted;

            if (model.IsFlagged && !sessionInfo.IsRestricted)
            {
                sessionInfo.FlaggedCount++;
                sessionInfo.IsRestricted = sessionInfo.FlaggedCount > 2;
                HttpContext.Session.SetObjectAsJson("SessionInfo", sessionInfo);
            }

            ViewBag.SessionInfo = sessionInfo;
            return View(model);
        }

        [HttpPost]
        public IActionResult SaveFeedback(string message)
        {
            var sessionInfo = HttpContext.Session.GetObjectFromJson<SessionInfo>("SessionInfo");
            if (sessionInfo == null || sessionInfo.IsRestricted)
            {
                return RedirectToAction("Index");
            }

            var feedback = new Feedback
            {
                CustomerId = sessionInfo.CustomerId,
                Message = message,
                Timestamp = DateTime.Now
            };

            _context.Feedbacks.Add(feedback);
            _context.SaveChanges();

            return RedirectToAction("Index");
        }

        [HttpPost]
        public IActionResult RemoveBlockedWords(string message)
        {
            var sessionInfo = HttpContext.Session.GetObjectFromJson<SessionInfo>("SessionInfo");
            if (sessionInfo == null)
            {
                return RedirectToAction("Index");
            }

            var blockedWords = _context.BlockedWords.ToList();
            var cleanedMessage = message;

            foreach (var blockedWord in blockedWords)
            {
                try
                {
                    var regex = new Regex(blockedWord.Pattern, RegexOptions.IgnoreCase);
                    cleanedMessage = regex.Replace(cleanedMessage, "[REMOVED]");
                }
                catch (ArgumentException)
                {
                    // If regex is invalid, treat as simple string replacement
                    cleanedMessage = cleanedMessage.Replace(blockedWord.Pattern, "[REMOVED]", StringComparison.OrdinalIgnoreCase);
                }
            }

            var model = new FeedbackViewModel
            {
                Message = cleanedMessage,
                CanSave = !sessionInfo.IsRestricted
            };

            ViewBag.SessionInfo = sessionInfo;
            return View("SubmitFeedback", model);
        }

        public IActionResult CustomerRegistration()
        {
            return View();
        }

        public IActionResult Error()
        {
            return View();
        }
    }
}