using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using lab10.Data;
using lab10.Models;

namespace lab10.Controllers
{
    [Authorize]
    public class MultimediaController : Controller
    {
        private readonly ApplicationDbContext _context;

        public MultimediaController(ApplicationDbContext context)
        {
            _context = context;
        }

        public async Task<IActionResult> Index()
        {
            var files = await _context.MultimediaFiles.ToListAsync();
            ViewBag.Total = files.Count;
            var genres = await _context.MultimediaFiles
                .Select(f => f.Genre)
                .Distinct()
                .OrderBy(g => g)
                .ToListAsync();
            
            ViewBag.Genres = genres;
            return View(files);
        }

        [HttpGet]
        public async Task<IActionResult> GetByGenre(string genre)
        {
            var files = string.IsNullOrEmpty(genre) || genre == "All" 
                ? await _context.MultimediaFiles.ToListAsync()
                : await _context.MultimediaFiles.Where(f => f.Genre == genre).ToListAsync();
            
            return Json(files);
        }

        public IActionResult Create()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Create(MultimediaFile file)
        {
            if (ModelState.IsValid)
            {
                _context.MultimediaFiles.Add(file);
                await _context.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            return View(file);
        }

        public async Task<IActionResult> Edit(int id)
        {
            var file = await _context.MultimediaFiles.FindAsync(id);
            if (file == null) return NotFound();
            return View(file);
        }

        [HttpPost]
        public async Task<IActionResult> Edit(MultimediaFile file)
        {
            if (ModelState.IsValid)
            {
                _context.Update(file);
                await _context.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            return View(file);
        }

        [HttpPost]
        public async Task<IActionResult> Delete(int id)
        {
            var file = await _context.MultimediaFiles.FindAsync(id);
            if (file != null)
            {
                _context.MultimediaFiles.Remove(file);
                await _context.SaveChangesAsync();
            }
            return RedirectToAction("Index");
        }
    }
} 