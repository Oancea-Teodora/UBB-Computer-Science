__author__ = 'dadi'

import socket
import threading
import random
import struct

random.seed()
start = 1;
stop = 2 ** 17 - 1
my_num = random.randint(start, stop)
print('Server number: ', my_num)

client_data = dict() # key: (ip, port), val: last number

if __name__ == '__main__':
    try:
        rs = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        rs.bind(('0.0.0.0', 1234))
        # rs.listen(5)
    except socket.error as msg:
        print(msg.strerror)
        exit(-1)

    while True:
        cnumber, addr = rs.recvfrom(4)
        cnumber = struct.unpack('!I', cnumber)[0]
        client_data[addr] = cnumber
        print(f'Received {cnumber} from {addr}; my number is {my_num}')
        if cnumber > my_num:
            rs.sendto(b'S', addr)
        if cnumber < my_num:
            rs.sendto(b'H', addr)
        if cnumber == my_num:
            print('We have a winner')
            client_guessed = True
            for addr in client_data:
                rs.sendto(b"G" if client_data[addr] == my_num else b"L", addr)
