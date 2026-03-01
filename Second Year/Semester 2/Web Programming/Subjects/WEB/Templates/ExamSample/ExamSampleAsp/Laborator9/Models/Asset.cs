using System;
namespace ExamTemplateAsp.Models
{
    public class Asset
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public int Value { get; set; }
    }
}
