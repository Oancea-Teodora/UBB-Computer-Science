using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using AuthorsDocumentsMovies.Models;

namespace AuthorsDocumentsMovies.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DocumentController : ControllerBase
    {
        private readonly AuthorsContext _context;

        public DocumentController(AuthorsContext context)
        {
            _context = context;
        }

        // GET: api/Document
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Document>>> GetDocuments()
        {
            return await _context.Documents.ToListAsync();
        }
        
        // GET: api/Document/Interleave1
        [HttpGet("Interleave1")]
        public async Task<ActionResult<IEnumerable<Document>>> GetInterleave1()
        {
            var username = HttpContext.Session.GetString("username");
            var author = _context.Authors.FirstOrDefault(author1 => author1.Name.Equals(username));
            
            
            
            return await _context.Documents.Where(document => author.DocumentList.Contains(document.Name)).ToListAsync();
        }
        
        // GET: api/Document/Interleave2
        [HttpGet("Interleave2")]
        public async Task<ActionResult<IEnumerable<Movie>>> GetInterleave2()
        {
            var username = HttpContext.Session.GetString("username");
            var author = _context.Authors.FirstOrDefault(author1 => author1.Name.Equals(username));
            
            
            
            return await _context.Movies.Where(movie => author.MovieList.Contains(movie.Title)).ToListAsync();
        }
        
        // GET: api/Document/MostFrequent
        [HttpGet("MostFrequent")]
        public async Task<ActionResult<IEnumerable<Document>>> GetMostFrequent()
        {

            string nameOfMostFrequent = "";
            int maximum = 0;


            var authors = _context.Authors.ToList();

            var documents = _context.Documents.ToList();
            
            foreach (var document in documents)
            {
                int counter = 0;
                foreach (var author in authors)
                {
                    if (author.DocumentList.Contains(document.Name))
                    {
                        counter++;
                    }
                }

                if (counter > maximum)
                {
                    maximum = counter;
                    nameOfMostFrequent = document.Name;
                }
                
            }
            
            return await _context.Documents.Where(document => document.Name.Equals(nameOfMostFrequent)).ToListAsync();
        }

        // GET: api/Document/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Document>> GetDocument(int id)
        {
            var document = await _context.Documents.FindAsync(id);

            if (document == null)
            {
                return NotFound();
            }

            return document;
        }

        // PUT: api/Document/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDocument(int id, Document document)
        {
            if (id != document.Id)
            {
                return BadRequest();
            }

            _context.Entry(document).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DocumentExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Document
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<IEnumerable<Document>>> PostDocument([FromForm]string name,[FromForm]string contents)
        {
            var document = new Document();
            document.Name = name;
            document.Contents = contents;
            _context.Documents.Add(document);
            await _context.SaveChangesAsync();

            return await _context.Documents.ToListAsync();
        }

        // DELETE: api/Document/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDocument(int id)
        {
            var document = await _context.Documents.FindAsync(id);
            if (document == null)
            {
                return NotFound();
            }

            _context.Documents.Remove(document);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DocumentExists(int id)
        {
            return _context.Documents.Any(e => e.Id == id);
        }
    }
}
