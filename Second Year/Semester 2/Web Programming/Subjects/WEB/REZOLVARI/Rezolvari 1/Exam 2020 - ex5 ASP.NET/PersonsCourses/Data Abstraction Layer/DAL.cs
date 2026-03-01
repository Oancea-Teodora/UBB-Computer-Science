using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;

namespace PersonsCourses.Data_Abstraction_Layer
{
    public class DAL {
        private MySqlConnection conn;

        public DAL()
        {
            this.conn = new MySqlConnection();
            this.conn.ConnectionString = "server=localhost;uid=root;pwd=;database=exam5;";
            conn.Open();
        }

        public int Authenticate(string name)
        {
            try
            {
                MySqlCommand cmd = new MySqlCommand("select id from person P where P.name=@name", conn);
                cmd.Parameters.AddWithValue("@name", name);
                cmd.Prepare();
                MySqlDataReader myreader = cmd.ExecuteReader();

                if (myreader.Read())
                {
                    return myreader.GetInt32("id");
                }
                myreader.Close();
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
            return -1;
        }

        public List<string> GetMyCourses(int id)
        {
            List<string> courses=new List<string>();
            try
            {
                MySqlCommand cmd = new MySqlCommand("select C.name from course C where C.proffesorId=@id", conn);
                cmd.Parameters.AddWithValue("@id", id);
                cmd.Prepare();
                MySqlDataReader myreader = cmd.ExecuteReader();

                while (myreader.Read())
                {
                    courses.Add(myreader.GetString("name"));
                }
                myreader.Close();
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
            return courses;
        }


        public String GetParticipants(string course)
        { 
            try
            {
                MySqlCommand cmd = new MySqlCommand("select participants from course C where C.name=@name", conn);
                cmd.Parameters.AddWithValue("@name", course);
                cmd.Prepare();
                MySqlDataReader myreader = cmd.ExecuteReader();

                if (myreader.Read())
                {
                    return myreader.GetString("participants");
                }
                myreader.Close();
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
            return null;
        }

        public List<String> GetCourses(string studentName)
        {
            List<string> courses = new List<string>();
            try
            {
                MySqlCommand cmd = new MySqlCommand("select * from course C", conn);
                cmd.Prepare();
                MySqlDataReader myreader = cmd.ExecuteReader();

                while (myreader.Read())
                {
                    string participants= myreader.GetString("participants");
                    string course = myreader.GetString("name");
                    if (participants.Contains(studentName))
                    {
                        courses.Add(course);
                    }
                }
                myreader.Close();
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
            return courses;
        }

        

        public string GetParticipantsAndGrades(string courseName)
        {
            try
            {
                MySqlCommand cmd = new MySqlCommand("select C.participants, C.grades from course C where C.name=@name", conn);
                cmd.Parameters.AddWithValue("@name", courseName);
                cmd.Prepare();
                MySqlDataReader myreader = cmd.ExecuteReader();

                if (myreader.Read())
                {
                    string grades= myreader.GetString("grades");
                    string participants = myreader.GetString("participants");

                    return grades + "+" + participants;
                    
                }
                myreader.Close();
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
            return null;
        }

        public void updateGrade(string course, string grades)
        {
            try
            {
                MySqlCommand cmd = new MySqlCommand("update Course C set C.grades=@grades where C.name=@name", conn);
                cmd.Parameters.AddWithValue("@grades", grades);
                cmd.Parameters.AddWithValue("@name", course);
                cmd.Prepare();
                MySqlDataReader myreader = cmd.ExecuteReader();

              
                myreader.Close();
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
       
        }

        public void AddStudent(string course, string grades, string participants)
        {
            try
            {
                MySqlCommand cmd = new MySqlCommand("update Course C set C.grades=@grades, C.participants=@participants where C.name=@name", conn);
                cmd.Parameters.AddWithValue("@grades", grades);
                cmd.Parameters.AddWithValue("@name", course);
                cmd.Parameters.AddWithValue("@participants", participants);
                cmd.Prepare();
                MySqlDataReader myreader = cmd.ExecuteReader();


                myreader.Close();
            }
            catch (MySqlException ex)
            {
                Console.Write(ex.Message);
            }
        }


    }
}
