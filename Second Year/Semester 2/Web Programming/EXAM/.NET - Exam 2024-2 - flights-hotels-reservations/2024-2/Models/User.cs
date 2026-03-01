using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class User
    {
        [Key]
        public int id { get; set; }

        public string username { get; set; }

    }
}
