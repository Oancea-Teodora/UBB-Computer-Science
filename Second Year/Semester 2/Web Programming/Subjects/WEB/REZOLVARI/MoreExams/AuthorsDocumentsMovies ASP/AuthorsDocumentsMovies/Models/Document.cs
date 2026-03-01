using System;
using System.Collections.Generic;

#nullable disable

namespace AuthorsDocumentsMovies.Models
{
    public partial class Document
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Contents { get; set; }
    }
}
