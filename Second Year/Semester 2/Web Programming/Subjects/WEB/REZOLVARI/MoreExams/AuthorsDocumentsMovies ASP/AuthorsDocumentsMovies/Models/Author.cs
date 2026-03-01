using System;
using System.Collections.Generic;

#nullable disable

namespace AuthorsDocumentsMovies.Models
{
    public partial class Author
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string DocumentList { get; set; }
        public string MovieList { get; set; }
    }
}
