using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class OrderItem
    {

        [Key]
        public int id { get; set; }

        public int orderID { get; set; }

        public int productID { get; set; }

    }
}
