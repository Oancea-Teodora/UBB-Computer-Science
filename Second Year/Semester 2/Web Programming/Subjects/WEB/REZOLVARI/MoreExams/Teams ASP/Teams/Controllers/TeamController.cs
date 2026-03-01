using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Teams.Models;

namespace Teams.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TeamController : ControllerBase
    {
        private readonly TeamsContext _context;

        public TeamController(TeamsContext context)
        {
            _context = context;
        }

        // GET: api/Team
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Team>>> GetTeams()
        {
            return await _context.Teams.ToListAsync();
        }

        // GET: api/Team/matei
        [HttpGet("UserTeams")]
        public async Task<ActionResult<IEnumerable<Team>>> GetTeamsOfUser()
        {
            var username = HttpContext.Session.GetString("username");
            
            var teams = _context.Teams.Where(team => team.Members.Contains(username)).ToListAsync();

            return await  teams;
        }
        
        [HttpPost]
        public async Task<ActionResult<IEnumerable<Team>>> AddPlayerToTeam([FromForm]string playerName,[FromForm]string teamsName,[FromForm]int age)
        {

            try
            {
                var player = _context.Players.FirstOrDefault(player1 => player1.Name.Equals(playerName));
                var playerAge = player.Age;
            }
            catch (Exception e)
            {
                var newPlayer = new Player();
                newPlayer.Name = playerName;
                newPlayer.Age = age;
                _context.Players.Add(newPlayer);
                await _context.SaveChangesAsync();
            }
            finally
            {
                var teams = teamsName.Split(";");

                foreach (var team in teams)
                {
                    var teamInDB = _context.Teams.FirstOrDefault(t => t.Name.Equals(team));

                    if (teamInDB != null)
                    {
                        teamInDB.Members += playerName + ";";
                        await _context.SaveChangesAsync();
                    }
                }
            }
            
            return await _context.Teams.ToListAsync();
        }

        // PUT: api/Team/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutTeam(int id, Team team)
        {
            if (id != team.Id)
            {
                return BadRequest();
            }

            _context.Entry(team).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TeamExists(id))
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



        // DELETE: api/Team/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteTeam(int id)
        {
            var team = await _context.Teams.FindAsync(id);
            if (team == null)
            {
                return NotFound();
            }

            _context.Teams.Remove(team);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool TeamExists(int id)
        {
            return _context.Teams.Any(e => e.Id == id);
        }
    }
}
