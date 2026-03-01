using _2024_2.Models;
using Microsoft.EntityFrameworkCore;

namespace _2024_2.Data
{
    public class AppDbContext : DbContext
    {

        private readonly IConfiguration _configuration;
        public AppDbContext(DbContextOptions<AppDbContext> opts, IConfiguration configuration) : base(opts)
        {
            _configuration = configuration;
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseSqlServer(_configuration.GetConnectionString("DefaultConnection"));
            }
            base.OnConfiguring(optionsBuilder);
        }

        public DbSet<BlockedWords> BlockedWords { get; set; }
        public DbSet<Feedback> Feedback { get; set; }
        public DbSet<Customer> Customer { get; set; }

    }
}
