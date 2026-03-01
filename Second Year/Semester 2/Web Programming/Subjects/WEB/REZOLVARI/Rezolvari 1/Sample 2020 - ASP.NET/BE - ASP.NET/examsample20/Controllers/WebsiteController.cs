using examsample20.Model;
using examsample20.Service;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace examsample20.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class WebsiteController : ControllerBase
    {
        private WebsiteService _service;

        public WebsiteController(WebsiteService srv)
        {
            _service = srv;
        }

        // GET: api/<RecipesController>
        [HttpGet]
        public List<Asset> GetAssets(int userid)
        {
            return _service.getAllBelongingTo(userid);
        }

        [HttpPost("add")]
        public Asset AddAsset(Asset asset)
        {
            return _service.addAsset(asset);
        }

        [HttpPost("addAssets")]
        public void AddAssets(List<Asset> assets)
        {
            for (int i = 0; i < assets.Count; i++)
                _service.addAsset(assets[i]);
        }

        [HttpGet("getPage")]
        public List<Asset> GetPage(int pageNo, int pageSize, int userId)
        {
            return _service.GetPage(pageNo, pageSize, userId);
        }
    }
}
