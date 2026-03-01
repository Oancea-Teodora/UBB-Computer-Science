using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class BlockedWords
    {
        [Key]
        public int id { get; set; }

        public string pattern { get; set; }

    }
}
