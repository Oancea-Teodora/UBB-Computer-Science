using sample.Context;
using sample.Model;

namespace sample.Services
{
    public class UserService
    {
        private ExamContext _context;

        public UserService(ExamContext context)
        {
            _context = context;
        }

        public bool Login(string username)
        {
            Console.WriteLine(username);
            SoftwareDeveloper user = _context.SoftwareDevelopers.FirstOrDefault(u => u.Name == username);

            if (user == null)
                return false;

            return true;
        }

        public int getUserId(string username)
        {
            SoftwareDeveloper u = _context.SoftwareDevelopers.FirstOrDefault(u => u.Name.Equals(username));

            if (u == null)
                return -1;
            else
                return u.Id;
        }
    }
}
