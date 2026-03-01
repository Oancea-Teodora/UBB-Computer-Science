using System.Runtime.CompilerServices;
using System.Text.Json;

namespace _2024_2.Helpers
{
    public static class SessionHelper
    {
        public static void SetObjectAsJson(this ISession sess, string key, object value)
        {
            sess.SetString(key, JsonSerializer.Serialize(value));
        }
        public static T GetObjectFromJson<T> (this ISession sess, string key)
        {
            var value = sess.GetString(key);
            if(value == null)
            {
                return default;
            }
            else
            {
                return JsonSerializer.Deserialize<T>(value);
            }
        }
    }
}
