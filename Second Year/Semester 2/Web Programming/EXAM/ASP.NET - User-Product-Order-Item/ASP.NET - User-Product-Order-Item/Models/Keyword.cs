using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class Keyword
    {
        [Key]
        public int ID { get; set; }

        public string key { get; set; }

        public string value { get; set; }

    }
}
