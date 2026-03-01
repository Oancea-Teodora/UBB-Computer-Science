using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace ExamTemplateAsp.Models
{
    public class User
    {
        public int Id { get; set; }

        [DisplayName("Username")]
        [Required(ErrorMessage = "This field is required.")]
        public string Username { get; set; }

        [DataType(DataType.Password)]
        [Required(ErrorMessage = "This field is required.")]
        public string Password { get; set; }

        public string LoginErrorMessage { get; set; }
    }
}
