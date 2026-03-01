using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class Order
    {
        [Key]
        public int id { get; set; }

        public int userID { get; set; }

        public double totalPrice { get; set; }

    }
}
