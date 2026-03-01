using MockExam1.Models;
using MockExam1.Persistence;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MockExam1.Controllers
{
    public class MainController : Controller
    {
        private DataPersistence dataPersistence = new DataPersistence();

        public ActionResult Index()
        {
            return View("MainView");
        }

        public void UpdatePost(string id, string topicName, string text)
        {
            int intId, intDate;
            string user = (string)Session["user"];
            string today = DateTime.Today.ToString();
            today = today.Substring(0, today.Length - 9).Replace(".", "");

            if (!int.TryParse(id, out intId) || !int.TryParse(today, out intDate))
            {
                return;
            }

            var topic = dataPersistence.FindTopicByName(topicName);
            if (topic == null)
            {
                dataPersistence.SaveTopic(new Models.Topic(-1, topicName));
                topic = dataPersistence.FindTopicByName(topicName);
            }

            dataPersistence.UpdatePost(new Models.Post(intId, user, topic.Id, text, intDate));
            return;
        }

        public void InsertPost(string id, string topicName, string text)
        {
            int intId, intDate;
            string user = (string)Session["user"];
            string today = DateTime.Today.ToString();
            today = today.Substring(0, today.Length - 9).Replace(".", "");

            if (!int.TryParse(id, out intId) || !int.TryParse(today, out intDate))
            {
                return;
            }

            var topic = dataPersistence.FindTopicByName(topicName);
            if (topic == null)
            {
                dataPersistence.SaveTopic(new Models.Topic(-1, topicName));
                topic = dataPersistence.FindTopicByName(topicName);
            }

            dataPersistence.SavePost(new Models.Post(-1, user, topic.Id, text, intDate));
            return;
        }

        public string FindLastAddedPost()
        {
            return dataPersistence.FindLastAddedPost().ToString();
        }
    }
}
