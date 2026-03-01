using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using TemplateForExamen.Models;
namespace TemplateForExamen.DataAbstractionLayer
{
    public class DAL
    {
        public User GetUserByUserAndPassword(string username, string password)
        {
            User user = null;
            MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "server=localhost;uid=root;pwd=;database=wddb;";
            try
            {
                conn = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                conn.Open();
                string query = "SELECT * FROM Users WHERE userName = @Username AND userPass = @Password";
                MySqlCommand cmd = new MySqlCommand(query, conn);
                cmd.Parameters.AddWithValue("@Username", username);
                cmd.Parameters.AddWithValue("@Password", password);
                using (MySqlDataReader reader = cmd.ExecuteReader())
                {
                    if (reader.Read())
                    {
                        user = new User
                        {
                            Id = (int)reader["userID"],
                            Username = reader["userName"].ToString(),

                            Password = reader["userPass"].ToString()
                        };
                    }
                }
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
            return user;
        }

        public List<Games> GetAllGames()
        {
            List<Games> gamesList = new List<Games>();
            MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "server=localhost;uid=root;pwd=;database=wddb;";
            try
            {
                conn = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                conn.Open();
                string query = "SELECT * FROM games";
                MySqlCommand cmd = new MySqlCommand(query, conn);
                using (MySqlDataReader reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        gamesList.Add(new Games
                        {
                            Id = reader.GetInt32(0),
                            Title = reader.GetString(1),

                            Genre = reader.GetString(2),
                            ReleaseDate = reader.GetDateTime(3).ToString("dd/MM/yyyy"),
                            Platform = reader.GetString(4),
                        }) ;
                    }
                }
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
            return gamesList;
        }
        public string AddGame(string title,string genre,string releaseDate,string platform)
        {
            MySqlConnection conn;
            string myConnectionString;

            myConnectionString = "server=localhost;uid=root;pwd=;database=wddb;";
            try
            {
                conn = new MySqlConnection
                {
                    ConnectionString = myConnectionString
                };
                conn.Open();
                string query = "INSERT INTO Games (title,genre,release_date,platform) VALUES (@Title,@Genre,@ReleaseDate,@Platform)";
                MySqlCommand cmd = new MySqlCommand(query, conn);

                cmd.Parameters.AddWithValue("@Title", title);
                cmd.Parameters.AddWithValue("@Genre", genre);

                cmd.Parameters.AddWithValue("@ReleaseDate", releaseDate);
                cmd.Parameters.AddWithValue("@Platform", platform);

                cmd.ExecuteNonQuery();
                return "Add succesful";
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
                return "Not succesfull add!";
            }
         }
    }
}