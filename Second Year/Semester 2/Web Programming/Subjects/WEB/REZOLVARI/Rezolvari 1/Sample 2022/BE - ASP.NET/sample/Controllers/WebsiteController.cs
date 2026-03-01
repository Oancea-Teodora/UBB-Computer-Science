using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using sample.Model;
using sample.Services;

namespace sample.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class WebsiteController : ControllerBase
    {

        private ProjectService _service;

        public WebsiteController(ProjectService srv)
        {
            _service = srv;
        }

        // GET: api/<RecipesController>
        [HttpGet("getBelongingTo")]
        public List<Project> GetProjectsBelongingTo(int userid)
        {
            return _service.getAllBelongingTo(userid);
        }

        [HttpGet("getAll")]
        public List<Project> GetProjects()
        {
            return _service.GetAll();
        }

        [HttpGet("getAllDevs")]
        public List<SoftwareDeveloper> getDevs()
        {
            return _service.getAllDevs();
        }

        [HttpGet("getProjects")]
        public List<Project> GetMemberOf(string name)
        {
            return _service.getProjectsMemberOf(name);
        }

        [HttpPost("addDev")]
        public void AddDev([FromBody]AddRequest request)
        {
            Console.WriteLine(request.Name + " " + request.List);
            _service.addDevToProjects(request.Name, request.List);
        }


    }
}
