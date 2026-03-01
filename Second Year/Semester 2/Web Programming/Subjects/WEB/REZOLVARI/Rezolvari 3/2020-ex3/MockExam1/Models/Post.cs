using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MockExam1.Models
{
    public class Post
    {
        private int id;
        private string user;
        private int topicId;
        private string text;
        private int date;

        public Post(int id, string user, int topicId, string text, int date)
        {
            this.id = id;
            this.user = user;
            this.topicId = topicId;
            this.text = text;
            this.date = date;
        }

        public int Id
        {
            get => id;
            set => id = value;
        }

        public string User
        {
            get => user;
            set => user = value;
        }

        public int TopicId
        {
            get => topicId;
            set => topicId = value;
        }

        public string Text
        {
            get => text;
            set => text = value;
        }

        public int Date
        {
            get => date;
            set => date = value;
        }

        public override string ToString()
        {
            return $"{id}, {user}, {topicId}, {text}, {date}";
        }
    }
}