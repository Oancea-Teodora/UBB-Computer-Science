using sample.Context;
using sample.Model;

namespace sample.Services
{
    public class ProjectService
    {
        private ExamContext _context;


        public ProjectService(ExamContext ctx)
        {
            _context = ctx;
        }

        public void addDevToProjects(string name, string projects)
        {
            // check if dev exists
            // SoftwareDeveloper user = _context.SoftwareDevelopers.FirstOrDefault(u => u.Name == username);


            SoftwareDeveloper user = _context.SoftwareDevelopers.FirstOrDefault(u => u.Name == name);

            if (user == null)
                return;

            // split projects by ,

            string[] projectList = projects.Split(",");
            foreach (string p in projectList)
            {
                Console.WriteLine(p);
                // check if each project exists

                Project project = _context.Projects.FirstOrDefault(u => u.Name == p);

                // if not, add it

                if (project == null)
                    _context.Projects.Add(new Project(p));
                _context.SaveChanges();

                // add dev to project by taking the members property - turning it into an array, adding the dev and turning it into a comma separated string
                project = _context.Projects.FirstOrDefault(u => u.Name == p);

                string[] members = project.Members.Split(",");
                project.Members = project.Members + "," + name;
                Console.WriteLine(project.Members);

            }



            _context.SaveChanges();

        }

        public List<SoftwareDeveloper> getAllDevs()
        {
            return _context.SoftwareDevelopers.ToList();
        }
     
        public List<Project> GetAll()
        {
            return _context.Projects.ToList();
        }

        // get assets belonging to user
        public List<Project> getAllBelongingTo(int userid)
        {
            // return _context.Recipes.Where(d => d.type.typeDescr.Contains(filter)).ToList();

            return _context.Projects.Where(a => a.ProjectManagerID.Equals(userid)).ToList();
        }

        public List<Project> getProjectsMemberOf(string name)
        {
            List<Project> list = _context.Projects.ToList();
            List<Project> finalList = new List<Project>();

            for(int i = 0; i < list.Count; i++)
            {
                Console.WriteLine(list[i].Members);
                string[] members = list[i].Members.Split(",");
                bool isMember = false;
                foreach(string member in members)
                {
                    if (member == name)
                        isMember = true;
                }

                if (isMember)
                    finalList.Add(list[i]);
            }


            return finalList;
            
        }
    }
}
