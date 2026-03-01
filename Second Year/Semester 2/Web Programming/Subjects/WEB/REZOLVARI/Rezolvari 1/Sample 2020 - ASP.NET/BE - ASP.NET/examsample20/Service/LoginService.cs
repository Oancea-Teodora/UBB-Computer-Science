using examsample20.Context;
using examsample20.Model;

namespace examsample20.Service
{
    public class UserService
    {
        private ExamContext _context;

        public UserService(ExamContext context)
        {
            _context = context;
        }

        public bool Login(string username, string password)
        {
            Console.WriteLine(username);
            User user = _context.Users.FirstOrDefault(u => u.Username == username);

            if (user == null)
                return false;
            //throw new Exception("Invalid user");

            //throw new Exception("User doesn't exist!");

            if (password == user.Password)
                return true;

            return false;
            // throw new Exception("Invalid input!");
        }

        public int getUserId(string username)
        {
            User u = _context.Users.FirstOrDefault(u => u.Username.Equals(username));

            if (u == null)
                return -1;
            else
                return u.Id;
        }

    }
}
