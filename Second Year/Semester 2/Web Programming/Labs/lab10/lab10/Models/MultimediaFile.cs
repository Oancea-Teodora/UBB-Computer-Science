using System.ComponentModel.DataAnnotations;

namespace lab10.Models
{
    public class MultimediaFile
    {
        public int Id { get; set; }
        
        [Required]
        public string Title { get; set; } = string.Empty;
        
        [Required]
        public string Format { get; set; } = string.Empty;
        
        [Required]
        public string Genre { get; set; } = string.Empty;
        
        public string Path { get; set; } = string.Empty;
    }
} 