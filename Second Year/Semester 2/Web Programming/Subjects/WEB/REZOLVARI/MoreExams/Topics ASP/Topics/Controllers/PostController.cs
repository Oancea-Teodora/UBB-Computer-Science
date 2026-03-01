using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Topics.Models;

namespace Topics.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PostController : ControllerBase
    {
        private readonly TopicsContext _context;

      

        public PostController(TopicsContext context)
        {
            _context = context;
        }

        // GET: api/Post
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Post>>> GetPosts()
        {
            return await _context.Posts.ToListAsync();
        }
        
        // GET: api/Post/New
        [HttpGet("New")]
        public ActionResult<string> Notify()
        {
            int localLength = _context.Posts.ToList().Count;
            var length = HttpContext.Session.GetInt32("length");
            if (localLength > length)
            {
                HttpContext.Session.SetInt32("length", localLength);
                string userName = HttpContext.Session.GetString("username");
                var post = _context.Posts.OrderBy(post => post.Id).Last();
                Console.WriteLine(post);
                if (post.User != userName)
                {
                    return post.ToString();
                }
            }

            return "false";
        }

        // GET: api/Post/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Post>> GetPost(int id)
        {
            var post = await _context.Posts.FindAsync(id);

            if (post == null)
            {
                return NotFound();
            }

            return post;
        }

        // PUT: api/Post/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<ActionResult<IEnumerable<Post>>> PutPost(int id, Post post)
        {
            if (id != post.Id)
            {
                return BadRequest();
            }

            post.User = HttpContext.Session.GetString("username");
            post.Date = DateTime.Today.Day;

            _context.Entry(post).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PostExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return await _context.Posts.ToListAsync();
        }

        // POST: api/Post
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<IEnumerable<Post>>> PostPost([FromForm]string topicName, [FromForm]string text)
        {
            int topicId = 0;
            try
            {
                topicId = _context.Topics.FirstOrDefault(topic => topic.Topicname == topicName).Id;
            }
            catch (Exception e)
            {
                var newTopic = new Topic();
                newTopic.Topicname = topicName;
                _context.Topics.Add(newTopic);
                await _context.SaveChangesAsync();
                topicId = _context.Topics.FirstOrDefault(topic => topic.Topicname == topicName).Id;
            }
            finally
            {
                var newPost = new Post();
                newPost.User = HttpContext.Session.GetString("username");
                newPost.Date = DateTime.Today.Day;
                newPost.Text = text;
                newPost.Topicid = topicId;
            
           
                _context.Posts.Add(newPost);
                await _context.SaveChangesAsync();
            }
            

            return await _context.Posts.ToListAsync();
        }

        // DELETE: api/Post/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletePost(int id)
        {
            var post = await _context.Posts.FindAsync(id);
            if (post == null)
            {
                return NotFound();
            }

            _context.Posts.Remove(post);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool PostExists(int id)
        {
            return _context.Posts.Any(e => e.Id == id);
        }
    }
}
