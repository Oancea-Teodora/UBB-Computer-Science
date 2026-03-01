using Microsoft.VisualBasic;
using System.ComponentModel.DataAnnotations;

namespace _2024_2.Models
{
    public class Feedback
    {
        [Key]
        public int id { get; set; }

        public int customerId { get; set; }

        public string text { get; set; }

        public DateTime timestamp { get; set; }


    }
}
