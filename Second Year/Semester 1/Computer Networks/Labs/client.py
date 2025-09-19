import socket
import select
import sys

BUFFER_SIZE = 256


def main(host, port):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        client_socket.connect((host, port))
    except Exception as e:
        print(f"Connection error: {e}")
        return

    print("Connected to the server. Type messages and press Enter to send.")

    while True:
        # Use select to handle reading from stdin and socket
        # sys.stdin works only on linux/ macOS
        sockets_list = [sys.stdin, client_socket]
        read_sockets, _, _ = select.select(sockets_list, [], [])

        for notified_socket in read_sockets:
            # Incoming message from the server
            if notified_socket == client_socket:
                message = client_socket.recv(BUFFER_SIZE)
                if not message:
                    print("Server closed connection.")
                    return
                print(message.decode(), end="")

            # User typing a message
            else:
                message = sys.stdin.readline()
                client_socket.send(message.encode())

    client_socket.close()


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python client.py <host> <port>")
    else:
        main(sys.argv[1], int(sys.argv[2]))