using examsample20.Context;
using examsample20.Model;

namespace examsample20.Service
{
    public class WebsiteService
    {
        private ExamContext _context;


        public WebsiteService(ExamContext ctx)
        {
            _context = ctx;
        }

        //public Recipe AddRecipe(RecipeDTO recipe)
        //{
        //    // check for invalid input - throw exceptions if type or author is null

        //    Author author = _context.Authors.FirstOrDefault(a => a.FirstName == recipe.FirstName && a.LastName == recipe.LastName);
        //    Type type = _context.Types.FirstOrDefault(t => t.typeDescr == recipe.typeDescr);

        //    Recipe new_recipe = new Recipe(author.Id, type.Id, recipe.Name, recipe.Description);
        //    _context.Recipes.Add(new_recipe);
        //    _context.SaveChanges();
        //    return new_recipe;
        //}

        public List<Asset> GetAll()
        {
            return _context.Assets.ToList();
        }

        // get assets belonging to user
        public List<Asset> getAllBelongingTo(int userid)
        {
            // return _context.Recipes.Where(d => d.type.typeDescr.Contains(filter)).ToList();

            return _context.Assets.Where(a => a.Userid.Equals(userid)).ToList();
        }

        public Asset addAsset(Asset asset)
        {
            ///  _context.Recipes.Add(new_recipe);
           // _context.SaveChanges();
            //return new_recipe;
            _context.Assets.Add(asset);
            _context.SaveChanges();
            return asset;

        }

        public List<Asset> GetPage(int pageNo, int pageSize, int userId)
        {
            // /determine the sql LIMIT starting number for the results on the displaying page
            //  $page_first_result = ($page - 1) * $results_per_page;
            //  $query = "SELECT * FROM websites  LIMIT " . $page_first_result . ',' . $results_per_page;
            // var pagedProductQuery = productQuery.Skip(25 * page).Take(25)
            List<Asset> pagedAssets = _context.Assets.Where(a => a.Userid.Equals(userId)).Skip((pageNo - 1) * pageSize).Take(pageSize).ToList();
            return pagedAssets;
        }
    }
}
