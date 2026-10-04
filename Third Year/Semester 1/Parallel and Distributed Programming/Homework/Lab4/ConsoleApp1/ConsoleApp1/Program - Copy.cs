//using System;
//using System.Net;
//using System.Net.Sockets;
//using System.Text;
//using System.Threading.Tasks;

//class Program
//{
//    class DownloadState
//    {
//        public Socket Socket;
//        public byte[] Buffer = new byte[4096];
//        public StringBuilder Response = new StringBuilder();
//        public string Url;
//        public string Host;
//        public string Path;
//    }

//    static void ParseUrl(string url, out string host, out string path)
//    {
//        if (url.StartsWith("http://"))
//            url = url.Substring("http://".Length);

//        int slash = url.IndexOf('/');
//        if (slash < 0)
//        {
//            host = url;
//            path = "/";
//        }
//        else
//        {
//            host = url.Substring(0, slash);
//            path = url.Substring(slash);
//        }
//    }

//    static int GetContentLength(string header)
//    {
//        string[] lines = header.Split('\n');
//        foreach (string rawLine in lines)
//        {
//            string line = rawLine.Trim();
//            if (line.StartsWith("Content-Length:", StringComparison.OrdinalIgnoreCase))
//            {
//                string val = line.Substring("Content-Length:".Length).Trim();
//                int len;
//                if (int.TryParse(val, out len))
//                    return len;
//            }
//        }
//        return 0;
//    }

//    static bool IsResponseComplete(string response)
//    {
//        int headerEnd = response.IndexOf("\r\n\r\n");
//        if (headerEnd < 0) return false;
//        string header = response.Substring(0, headerEnd);
//        int contentLength = GetContentLength(header);
//        int bodyLen = response.Length - (headerEnd + 4);
//        return contentLength > 0 && bodyLen >= contentLength;
//    }

//    static void PrintBody(DownloadState st)
//    {
//        string full = st.Response.ToString();
//        int headerEnd = full.IndexOf("\r\n\r\n");
//        string body = (headerEnd >= 0) ? full.Substring(headerEnd + 4) : full;

//        Console.WriteLine(st.Url);
//        int toShow = Math.Min(body.Length, 200);
//        Console.WriteLine(body.Substring(0, toShow));
//        Console.WriteLine("length: " + body.Length + " characters\n");
//    }

//    static Task ConnectAsync(Socket s, EndPoint ep)
//    {
//        var tcs = new TaskCompletionSource<bool>();
//        s.BeginConnect(ep, ar =>
//        {
//            try { s.EndConnect(ar); tcs.SetResult(true); }
//            catch (Exception ex) { tcs.SetException(ex); }
//        }, null);
//        return tcs.Task;
//    }

//    static Task<int> SendAsync(Socket s, byte[] buf, int offset, int count)
//    {
//        var tcs = new TaskCompletionSource<int>();
//        s.BeginSend(buf, offset, count, SocketFlags.None, ar =>
//        {
//            try { tcs.SetResult(s.EndSend(ar)); }
//            catch (Exception ex) { tcs.SetException(ex); }
//        }, null);
//        return tcs.Task;
//    }

//    static Task<int> ReceiveAsync(Socket s, byte[] buf, int offset, int count)
//    {
//        var tcs = new TaskCompletionSource<int>();
//        s.BeginReceive(buf, offset, count, SocketFlags.None, ar =>
//        {
//            try { tcs.SetResult(s.EndReceive(ar)); }
//            catch (Exception ex) { tcs.SetException(ex); }
//        }, null);
//        return tcs.Task;
//    }

//    static Task ReceiveAllAsync(DownloadState st)
//    {
//        var tcs = new TaskCompletionSource<bool>();

//        Action<Task<int>> step = null;
//        step = t =>
//        {
//            int n = t.Result;
//            if (n <= 0)
//            {
//                tcs.SetResult(true);
//                return;
//            }

//            st.Response.Append(Encoding.ASCII.GetString(st.Buffer, 0, n));

//            if (IsResponseComplete(st.Response.ToString()))
//            {
//                tcs.SetResult(true);
//                return;
//            }

//            ReceiveAsync(st.Socket, st.Buffer, 0, st.Buffer.Length).ContinueWith(step);
//        };

//        ReceiveAsync(st.Socket, st.Buffer, 0, st.Buffer.Length).ContinueWith(step);

//        return tcs.Task;
//    }

//    static Task DownloadOneAsync(string url)
//    {
//        var st = new DownloadState();
//        st.Url = url;
//        ParseUrl(url, out st.Host, out st.Path);

//        IPHostEntry entry = Dns.GetHostEntry(st.Host);
//        IPAddress ip = null;
//        foreach (var a in entry.AddressList)
//            if (a.AddressFamily == AddressFamily.InterNetwork) { ip = a; break; }
//        if (ip == null)
//        {
//            Console.WriteLine("No IPv4 for " + st.Host);
//            return Task.CompletedTask;
//        }

//        st.Socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
//        var ep = new IPEndPoint(ip, 80);

//        var tcs = new TaskCompletionSource<bool>();

//        ConnectAsync(st.Socket, ep).ContinueWith(t1 =>
//        {
//            if (t1.IsFaulted)
//            {
//                Console.WriteLine("Connect error for " + url);
//                tcs.SetResult(true);
//                return;
//            }

//            string request =
//                "GET " + st.Path + " HTTP/1.1\r\n" +
//                "Host: " + st.Host + "\r\n" +
//                "Connection: close\r\n\r\n";
//            byte[] reqBytes = Encoding.ASCII.GetBytes(request);

//            SendAsync(st.Socket, reqBytes, 0, reqBytes.Length).ContinueWith(t2 =>
//            {
//                if (t2.IsFaulted)
//                {
//                    Console.WriteLine("Send error for " + url);
//                    tcs.SetResult(true);
//                    return;
//                }

//                ReceiveAllAsync(st).ContinueWith(t3 =>
//                {
//                    PrintBody(st);
//                    try { st.Socket.Shutdown(SocketShutdown.Both); } catch { }
//                    st.Socket.Close();
//                    tcs.SetResult(true);
//                });
//            });
//        });

//        return tcs.Task;
//    }

//    static void Main(string[] args)
//    {
//        string[] urls =
//        {
//            "http://example.org/",
//            "http://example.com/",
//        };

//        Task[] tasks = new Task[urls.Length];
//        for (int i = 0; i < urls.Length; i++)
//            tasks[i] = DownloadOneAsync(urls[i]);

//        Task.WaitAll(tasks);
//        Console.WriteLine("All downloads done (ContinueWith).");
//    }
//}
