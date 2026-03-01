using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MockExam1.Models
{
    public class Topic
    {
        private int id;
        private string name;

        public Topic(int id, string name)
        {
            this.id = id;
            this.name = name;
        }

        public int Id
        {
            get => id;
            set => id = value;
        }

        public string Name
        {
            get => name;
            set => name = value;
        }
    }
}