using System;
using System.Net;
using System.Net.Sockets;
using System.Text;

class Program
{
    class DownloadState
    {
        public Socket Socket;
        public byte[] Buffer = new byte[4096];
        public StringBuilder Response = new StringBuilder();
        public string Url;
        public string Host;
        public string Path;
    }

    static CountdownEvent done;

    static void ParseUrl(string url, out string host, out string path)
    {
        if (url.StartsWith("http://"))
            url = url.Substring("http://".Length);

        int slash = url.IndexOf('/');
        if (slash < 0)
        {
            host = url;
            path = "/";
        }
        else
        {
            host = url.Substring(0, slash);
            path = url.Substring(slash);
        }
    }

    static int GetContentLength(string header)
    {
        string[] lines = header.Split('\n');
        foreach (string rawLine in lines)
        {
            string line = rawLine.Trim();
            if (line.StartsWith("Content-Length:", StringComparison.OrdinalIgnoreCase))
            {
                string val = line.Substring("Content-Length:".Length).Trim();
                int len;
                if (int.TryParse(val, out len))
                    return len;
            }
        }
        return 0;
    }

    static bool IsResponseComplete(string response)
    {
        int headerEnd = response.IndexOf("\r\n\r\n");
        if (headerEnd < 0) return false;

        string header = response.Substring(0, headerEnd);
        int contentLength = GetContentLength(header);
        int bodyLen = response.Length - (headerEnd + 4);
        return contentLength > 0 && bodyLen >= contentLength;
    }

    static void PrintBody(DownloadState st)
    {
        string full = st.Response.ToString();
        int headerEnd = full.IndexOf("\r\n\r\n");
        string body = (headerEnd >= 0) ? full.Substring(headerEnd + 4) : full;

        Console.WriteLine(st.Url);
        int toShow = Math.Min(body.Length, 200);
        Console.WriteLine(body.Substring(0, toShow));
        Console.WriteLine("length: " + body.Length + " characters\n");
    }

    static void StartDownload(string url)
    {
        var st = new DownloadState();
        st.Url = url;
        ParseUrl(url, out st.Host, out st.Path);

        IPHostEntry entry = Dns.GetHostEntry(st.Host);
        IPAddress ip = null;
        foreach (var a in entry.AddressList)
            if (a.AddressFamily == AddressFamily.InterNetwork) { ip = a; break; }
        if (ip == null)
        {
            Console.WriteLine("No IPv4 for " + st.Host);
            return;
        }

        st.Socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        var ep = new IPEndPoint(ip, 80);

        st.Socket.BeginConnect(ep, ar =>
        {
            try
            {
                st.Socket.EndConnect(ar);

                string request =
                    "GET " + st.Path + " HTTP/1.1\r\n" +
                    "Host: " + st.Host + "\r\n" +
                    "Connection: close\r\n\r\n";

                byte[] reqBytes = Encoding.ASCII.GetBytes(request);
                st.Socket.BeginSend(reqBytes, 0, reqBytes.Length, SocketFlags.None, SendCallback, st);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Connect error for " + url + ": " + ex.Message);
                try { st.Socket.Close(); } catch { }
            }
        }, null);
    }

    static void SendCallback(IAsyncResult ar)
    {
        var st = (DownloadState)ar.AsyncState;
        try
        {
            st.Socket.EndSend(ar);
            st.Socket.BeginReceive(st.Buffer, 0, st.Buffer.Length, SocketFlags.None, ReceiveCallback, st);
        }
        catch (Exception ex)
        {
            Console.WriteLine("Send error for " + st.Url + ": " + ex.Message);
            try { st.Socket.Close(); } catch { }
        }
    }

    static void ReceiveCallback(IAsyncResult ar)
    {
        var st = (DownloadState)ar.AsyncState;
        try
        {
            int n = st.Socket.EndReceive(ar);
            if (n <= 0)
            {
                PrintBody(st);
                st.Socket.Close();
                done.Signal();
                return;
            }

            st.Response.Append(Encoding.ASCII.GetString(st.Buffer, 0, n));

            if (IsResponseComplete(st.Response.ToString()))
            {
                PrintBody(st);
                st.Socket.Close();
                done.Signal();
            }
            else
            {
                st.Socket.BeginReceive(st.Buffer, 0, st.Buffer.Length, SocketFlags.None, ReceiveCallback, st);
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("Receive error for " + st.Url + ": " + ex.Message);
            try { st.Socket.Close(); } catch { }
            done.Signal();
        }
    }

    static void Main(string[] args)
    {
        string[] urls =
        {
            "http://example.org/",
            "http://example.com/",
        };

        done = new CountdownEvent(urls.Length);

        foreach (var url in urls)
            StartDownload(url);

        done.Wait();
        Console.WriteLine("All downloads finished.");
    }
}
