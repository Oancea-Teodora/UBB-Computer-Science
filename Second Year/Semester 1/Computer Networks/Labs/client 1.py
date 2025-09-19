import select
import socket
import threading
import sys


def read_from_keyboard(sock):
    while True:
        inp = input()
        sock.send(inp.encode())



if __name__ == '__main__':
    sock = socket.create_connection(("127.0.0.1", 1234))
    # r_list = [sock, sys.stdin]
    # while True:
    #     reading_list = select.select(r_list, [], [])[0]
    #     for s in reading_list:
    #         if s is sock:
    #             message = s.recv(1024)
    #             print(message.decode())
    #         else:
    #             from_input = sys.stdin.readline()
    #             sock.send(from_input.encode())
    read_thread = threading.Thread(target=read_from_keyboard, args=(sock,))
    read_thread.start()
    while True:
        data = sock.recv(1024)
        print(data.decode())
