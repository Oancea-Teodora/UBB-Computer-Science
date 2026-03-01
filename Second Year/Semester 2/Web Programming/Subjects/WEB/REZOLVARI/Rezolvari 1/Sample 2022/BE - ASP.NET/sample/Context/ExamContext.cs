using Microsoft.EntityFrameworkCore;
using sample.Model;

namespace sample.Context
{
    public class ExamContext : DbContext
    {
        public DbSet<SoftwareDeveloper> SoftwareDevelopers { get; set; }

        public DbSet<Project> Projects { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseSqlServer(@"Server=localhost;Database=Sample;Trusted_Connection=True;");
        }
    }
}
