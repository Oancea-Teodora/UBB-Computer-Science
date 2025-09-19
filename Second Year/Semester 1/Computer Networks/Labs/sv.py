import socket
import select

def send_to_all(sockets, message, crt_socket):
    for sock in sockets:
        if sock != crt_socket:
            sock.send(message.encode())


if __name__ == "__main__":
    try:
        welcome_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        welcome_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        welcome_socket.bind(('0.0.0.0', 1234))
        welcome_socket.listen(5)
    except socket.error:
        exit(1)

    reading_sockets = [welcome_socket]

    while True:
        ready_read = select.select(reading_sockets, [], [])[0]
        for ready_socket in ready_read:
            if ready_socket == welcome_socket:
                client_socket, addr = welcome_socket.accept()
                print(f"Accepted connection from {addr}")
                reading_sockets.append(client_socket)
                client_socket.send(b"Successfully connected")
            else:
                message = ready_socket.recv(1024)
                message = message.decode("ascii")
                if len (message) == 0:
                    print("Connection closed")
                    reading_sockets.remove(ready_socket)
                send_to_all(reading_sockets[1:], message, ready_socket)

