using System;
using System.Collections.Generic;

namespace Teams.Models
{
    public partial class Team
    {
        public int Id { get; set; }
        public int CaptainId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public string Members { get; set; }
    }
}
