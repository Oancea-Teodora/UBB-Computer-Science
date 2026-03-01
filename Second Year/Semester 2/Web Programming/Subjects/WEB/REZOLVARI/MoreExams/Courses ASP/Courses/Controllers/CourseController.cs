using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Courses.Models;

namespace Courses.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CourseController : ControllerBase
    {
        private readonly CoursesContext _context;

        public CourseController(CoursesContext context)
        {
            _context = context;
        }

        // GET: api/Course
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Course>>> GetCourses()
        {
            return await _context.Courses.ToListAsync();
        }
        
        // GET: api/Course/name
        [HttpGet("{name}")]
        public ActionResult<string> GetChannels(string name)
        {
            var participants = _context.Courses.Where(course => course.Coursename == name).FirstOrDefault()
                .Participants;

            return participants;
        }

        // GET: api/Course/Courses/name
        [HttpGet("Courses/{name}")]
        public async Task<ActionResult<IEnumerable<Course>>> GetSpecificCourses(string name)
        {
            var courses = _context.Courses.Where(course => course.Participants.Contains(name)).ToListAsync();
            return await courses;
        }

        
        // // GET: api/Course/5
        // [HttpGet("{id}")]
        // public async Task<ActionResult<Course>> GetCourse(int id)
        // {
        //     var course = await _context.Courses.FindAsync(id);
        //
        //     if (course == null)
        //     {
        //         return NotFound();
        //     }
        //
        //     return course;
        // }

        // PUT: api/Course/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutCourse(int id, Course course)
        {
            if (id != course.Id)
            {
                return BadRequest();
            }

            _context.Entry(course).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CourseExists(id))
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


        // DELETE: api/Course/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCourse(int id)
        {
            var course = await _context.Courses.FindAsync(id);
            if (course == null)
            {
                return NotFound();
            }

            _context.Courses.Remove(course);
            await _context.SaveChangesAsync();

            return NoContent();
        }
        
        [HttpPost]
        public async Task<ActionResult<IEnumerable<Course>>> AddGrade([FromForm]string studentName,[FromForm]string courseName,[FromForm]string grade)
        {
            var course = _context.Courses.FirstOrDefault(course1 => course1.Coursename.Equals(courseName));

            string studentAndGrade = studentName + "-" + grade;
            string minusAndGrade = "-" + grade;

            if (course.Participants.Contains(studentName))
            {
                if (course.Grades.Contains(studentName))
                {
                    string pattern = $"(?<={studentName})(.*?)(?=;)";
                    var grades = Regex.Replace(course.Participants, pattern, minusAndGrade);
                    course.Grades = grades;
                }
                else
                {
                    course.Grades += studentAndGrade + ";";
                }
               
            }
            else
            {
                course.Participants += studentName + ";";
                course.Grades += studentAndGrade + ";";
                
            }
            await _context.SaveChangesAsync();
            return await _context.Courses.ToListAsync();
        }

        private bool CourseExists(int id)
        {
            return _context.Courses.Any(e => e.Id == id);
        }
    }
}
