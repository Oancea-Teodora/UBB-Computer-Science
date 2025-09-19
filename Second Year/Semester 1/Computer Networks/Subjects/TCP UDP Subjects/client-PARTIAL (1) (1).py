"""
Implement a server application that generates a random integer P and communicates to the clients the number of digits of
that number – when the client connects.

Implement a client application that receives from a server the number of digits of P. The client generates a random
digit every N seconds and sends the digit to the server. The server answers with the positions where the digit is found
on the server’s number.

Spawn multiple clients. The server will announce all clients when it has a winner using UDP. You should design a method
for the server to infer the IP and port where is should communicate with each peer over UDP.

"""

import random
import socket
import struct
import time

IP = '127.0.0.1'
PORT = 7000
SERV_ADDR = (IP, PORT)

new_port = random.randint(1000, 10000)

if __name__ == '__main__':
    client_sock = None
    try:
        client_sock = socket.create_connection(SERV_ADDR)
    except socket.error as msg:
        print("Error: ", msg.strerror)
        exit(-1)

    size = struct.unpack("!i", client_sock.recv(4))[0]
    message = client_sock.recv(size)
    print(message.decode('ascii'))
    number_of_digits = struct.unpack("!i", client_sock.recv(4))[0]
    print(number_of_digits)

    step_count = 0
    finished = False

    while not finished:
        my_digit = random.randint(0, 9)
        print(f"My digit is: {my_digit}")

        try:
            client_sock.sendall(struct.pack("!i", my_digit))
            size = struct.unpack("!i", client_sock.recv(4))[0]
        except socket.error as msg:
            print("Error: ", msg.strerror)
            client_sock.close()
            exit(-2)

        if size > 0:
            positions = []
            for _ in range(size):
                pos = struct.unpack("!i", client_sock.recv(4))[0]
                positions.append(pos)
            print(f"Digit found on positions: {positions}")
        if size == 0:
            print("Digit is not in the number")
        if size == -1:
            print("You win!")
            finished = True
        if size == -2:
            print("You lose!")
            finished = True

        time.sleep(0.25)


    client_sock.sendall(struct.pack("!i", new_port))
    udp_socket = None
    try:
        udp_socket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
        udp_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

        udp_socket.bind((IP, new_port))
    except socket.error as msg:
        print("Error: ", msg.strerror)
        exit(-1)

    client_sock.close()
    print("\nUDP server up and listening")
    print(udp_socket.getsockname())
    message, addr = udp_socket.recvfrom(1000)
    print(message.decode())
    udp_socket.close()