using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class Product
    {
        [Key]
        public int id { get; set; }

        public string name { get; set; }

        public double price { get; set; }


    }
}
