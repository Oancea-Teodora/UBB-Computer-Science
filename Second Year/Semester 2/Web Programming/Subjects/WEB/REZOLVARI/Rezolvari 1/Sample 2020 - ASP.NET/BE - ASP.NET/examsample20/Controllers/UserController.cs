using examsample20.DTO;
using examsample20.Service;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace examsample20.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private UserService _service;

        public UserController(UserService service)
        {
            _service = service;
        }

        [HttpGet("getUserId")]
        public int getUserId(string username)
        {
            // string value = Request.QueryString("hello");
            // string username = Request.QueryString("username").toString();
            
            Console.WriteLine(username);
            return _service.getUserId(username);
            // Console.WriteLine(username);
        }

        //[AllowAnonymous]
        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest request)
        {
            try
            {
                var res = _service.Login(request.Username, request.Password);
                return Ok(res);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }

            if (request == null)
                return BadRequest("Invalid client request");

        }

      
    }
}
