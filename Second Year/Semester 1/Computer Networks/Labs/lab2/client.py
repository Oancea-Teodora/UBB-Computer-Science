__author__ = 'dadi'
import socket
import struct
import random

if __name__ == '__main__':
    try:
        s = socket.create_connection(('localhost', 1235))
    except socket.error as msg:
        print("Error: ", msg.strerror)
        exit(-1)

    finished = False
    sr = 1.0
    er = 1000.0
    random.seed()

    data = s.recv(1024)
    # display greeting message from server
    print(data.decode('ascii'))

    # generate a SINGLE random number

    my_num = random.uniform(sr, er)
    try:
        # we no longer need the while, because the client sends a single numer
        # pack the real number: ! - network encoding, d double
        s.sendall(struct.pack('!d', my_num))
        # receive the message from the server
        # the server will respond with a message
        answer = s.recv(1024)
    except socket.error as msg:
        print('Error: ', msg.strerror)
        s.close()
        exit(-2)

    # display the message from the server
    print(f'Sent {my_num:.4f}, Server response: {answer.decode("ascii")}')

    if b'You have the best guess' in answer:
        print(f"I am the winner with {my_num:.4f}")
    elif b'You lost' in answer:
        print(f"I lost with {my_num:.4f} ")

    # close the connection
    s.close()
