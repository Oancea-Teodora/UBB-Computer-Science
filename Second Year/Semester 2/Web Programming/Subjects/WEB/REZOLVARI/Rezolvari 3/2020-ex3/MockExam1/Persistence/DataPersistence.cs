using MockExam1.Models;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MockExam1.Persistence
{
    public class DataPersistence
    {
        private static string host = "localhost";
        private static string username = "postgres";
        private static string password = "postgres";
        private static string database = "web-2020-ex3";
        private string connectionString = $"Host={host};Username={username};Password={password};Database={database};";

        public void SaveTopic(Topic topic)
        {
            try
            {
                var conn = new NpgsqlConnection(connectionString);
                conn.Open();

                var sql = $"INSERT INTO public.\"Topics\"(name) VALUES ('{topic.Name}');";
                var cmd = new NpgsqlCommand(sql, conn);
                cmd.ExecuteNonQuery();
            }
            catch (Npgsql.NpgsqlException ex)
            {
                Console.Write(ex.Message);
            }
        }

        public void SavePost(Post post)
        {
            try
            {
                var conn = new NpgsqlConnection(connectionString);
                conn.Open();

                var sql = $"INSERT INTO public.\"Posts\"(\"user\", topicid, text, date) VALUES ('{post.User}', {post.TopicId}, '{post.Text}', {post.Date});";
                var cmd = new NpgsqlCommand(sql, conn);
                cmd.ExecuteNonQuery();
            }
            catch (Npgsql.NpgsqlException ex)
            {
                Console.Write(ex.Message);
            }
        }

        public void UpdatePost(Post post)
        {
            try
            {
                var conn = new NpgsqlConnection(connectionString);
                conn.Open();

                var sql = $"UPDATE public.\"Posts\" SET \"user\" = '{post.User}', topicid = {post.TopicId}, text = '{post.Text}', date = {post.Date} WHERE id = {post.Id};";
                var cmd = new NpgsqlCommand(sql, conn);
                cmd.ExecuteNonQuery();
            }
            catch (Npgsql.NpgsqlException ex)
            {
                Console.Write(ex.Message);
            }
        }

        public Topic FindTopicByName(string name)
        {
            var conn = new NpgsqlConnection(connectionString);
            conn.Open();

            var sql = $"SELECT * FROM public.\"Topics\" WHERE name = '{name}';";

            List<Topic> products = new List<Topic>();
            var cmd = new NpgsqlCommand(sql, conn);

            NpgsqlDataReader npgsqlDataReader = cmd.ExecuteReader();
            while (npgsqlDataReader.Read())
            {
                products.Add(new Topic(npgsqlDataReader.GetInt32(0), npgsqlDataReader.GetString(1)));
            }

            return products.Count != 1 ? null : products[0];
        }

        public Post FindLastAddedPost()
        {
            var conn = new NpgsqlConnection(connectionString);
            conn.Open();

            var sql = $"SELECT* FROM public.\"Posts\" ORDER BY id DESC LIMIT 1";

            List<Post> posts = new List<Post>();
            var cmd = new NpgsqlCommand(sql, conn);

            NpgsqlDataReader npgsqlDataReader = cmd.ExecuteReader();
            while (npgsqlDataReader.Read())
            {
                posts.Add(new Post(npgsqlDataReader.GetInt32(0), npgsqlDataReader.GetString(1), npgsqlDataReader.GetInt32(2), npgsqlDataReader.GetString(3), npgsqlDataReader.GetInt32(4)));
            }

            return posts.Count != 1 ? null : posts[0];

            
        }
    }
}
