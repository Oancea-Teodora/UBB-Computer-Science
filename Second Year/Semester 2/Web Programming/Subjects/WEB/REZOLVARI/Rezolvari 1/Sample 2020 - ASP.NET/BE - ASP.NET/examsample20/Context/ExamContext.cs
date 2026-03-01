using examsample20.Model;
using Microsoft.EntityFrameworkCore;

namespace examsample20.Context
{
    public class ExamContext : DbContext
    {

        public DbSet<User> Users { get; set; }

        public DbSet<Asset> Assets { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseSqlServer(@"Server=localhost;Database=Assets;Trusted_Connection=True;");
        }
    }
}
