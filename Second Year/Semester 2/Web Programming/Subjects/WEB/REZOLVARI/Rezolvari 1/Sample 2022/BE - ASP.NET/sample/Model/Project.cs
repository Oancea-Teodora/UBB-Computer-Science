namespace sample.Model
{
    public class Project
    {
        //  table Project: id (int), ProjectManagerID (int), name (string), description (string),   members(string)

        public Project(string n)
        {
            this.Name = n;
            this.Members = "";
            this.Description = "";
            this.ProjectManagerID = 1;
        }

        public Project()
        {

        }
    
        public int Id { get; set; }
        public string Name { get; set; }

        public int ProjectManagerID { get; set; }

        public string Description { get; set; }
        public string Members { get; set; }
    }
}
