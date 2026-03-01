using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace TemplateForExamen.Models
{
    public class Games
    {
        public int Id { get; set; }

        public string Title { get; set; }

        public string Genre { get; set; }

        public string ReleaseDate { get; set; }
        public string Platform { get; set; }
    }
}