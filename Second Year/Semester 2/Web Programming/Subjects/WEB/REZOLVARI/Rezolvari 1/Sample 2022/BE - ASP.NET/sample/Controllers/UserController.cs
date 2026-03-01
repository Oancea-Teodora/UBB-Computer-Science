using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using sample.Model;
using sample.Services;

namespace sample.Controllers
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
        public IActionResult Login([FromBody]Request request)
        {
            try
            {
                var res = _service.Login(request.Name);
                Console.WriteLine(res); 
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
