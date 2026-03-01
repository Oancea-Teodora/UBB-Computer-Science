using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class Template
    {
        [Key]
        public int ID { get; set; }

        public string name { get; set; }

        public string textContent { get; set; }


    }
}
