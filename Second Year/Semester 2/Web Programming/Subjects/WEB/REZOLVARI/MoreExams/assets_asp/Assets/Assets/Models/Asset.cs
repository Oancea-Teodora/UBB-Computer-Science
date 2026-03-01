using System;
using System.Collections.Generic;

#nullable disable

namespace Assets.Models
{
    public partial class Asset
    {
        public int Id { get; set; }
        public int Userid { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public int Value { get; set; }
        
    }
}
