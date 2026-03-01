using System;
using System.Collections.Generic;

namespace Topics.Models
{
    public partial class Post
    {
        public int Id { get; set; }
        public string User { get; set; }
        public int Topicid { get; set; }
        public string Text { get; set; }
        public int Date { get; set; }

        public override string ToString()
        {
            return $"{nameof(User)}: {User}, {nameof(Topicid)}: {Topicid}, {nameof(Text)}: {Text}, {nameof(Date)}: {Date}";
        }
    }
}
