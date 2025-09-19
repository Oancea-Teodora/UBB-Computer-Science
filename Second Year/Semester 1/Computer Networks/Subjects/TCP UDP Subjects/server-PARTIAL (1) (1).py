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
import threading

IP = '0.0.0.0'
PORT = 7000
SERV_ADDR = (IP, PORT)


threads = []
clients = []
new_clients = []
client_count = 0
client_guessed = False
my_lock = threading.Lock()
winner_thread = 0
winner_client = None

random.seed()
start = 1
stop = 2**17-1
my_num = random.randint(start, stop)
number_of_digits = int(len(str(my_num)))

e = threading.Event()
e.clear()


def announceWinner():
    global clients, winner_client
    udp_socket = None
    try:
        udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        udp_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    except socket.error as msg:
        print("Error: ", msg.strerror)
        exit(-1)

    print("UDP server up and listening")
    win_msg = "you win!!"
    lose_msg = "you lost!!"

    for client in new_clients:
        if client == winner_client:
            udp_socket.sendto(win_msg.encode(), client)
        else:
            udp_socket.sendto(lose_msg.encode(), client)

    udp_socket.close()


def resetServ():
    global threads, client_guessed, winner_thread, client_count, winner_client
    while True:
        e.wait()
        for t in threads:
            t.join()
        print("all threads are finished now")


        my_lock.acquire()
        announceWinner()
        threads = []
        client_guessed = False
        winner_thread = -1
        client_count = 0
        my_num = random.randint(start, stop)
        print('\n\n -------------------- RESETTING SERVER')
        print('Server number: ', my_num)
        my_lock.release()
        e.clear()


def worker(client_socket):
    global client_count, number_of_digits, client_guessed, my_num, winner_thread, winner_client
    number_of_remaining_digits = number_of_digits
    digits = {}
    my_id_count = client_count
    print(f"Client #{my_id_count} from: {client_socket.getpeername()}")
    message = f"Hello client #{my_id_count}! You are entering the number guess competition now. The number of digits " \
              f"is:{number_of_digits}"
    client_socket.sendall(struct.pack("!i", len(message)))
    client_socket.sendall(bytes(message, 'ascii'))
    client_socket.sendall(struct.pack("!i", number_of_digits))

    while not client_guessed:
        try:
            client_digit = client_socket.recv(4)
            client_digit = struct.unpack("!i", client_digit)[0]

            positions = [pos for pos, digit in enumerate(str(my_num)) if int(digit) == client_digit]

            client_socket.sendall(struct.pack("!i", len(positions)))
            if len(positions):
                for pos in positions:
                    client_socket.sendall(struct.pack("!i", pos))

            if client_digit not in digits.keys():
                number_of_remaining_digits -= len(positions)
            digits[client_digit] = len(positions)

            if number_of_remaining_digits == 0:
                my_lock.acquire()
                client_guessed = True
                winner_thread = threading.get_ident()
                my_lock.release()
        except socket.error as msg:
            print('Error:', msg.strerror)
            break

    client_digit = client_socket.recv(4)


    if client_guessed:
        if threading.get_ident() == winner_thread:
            client_socket.sendall(struct.pack("!i", -1))
            new_port = struct.unpack("!i", client_socket.recv(4))[0]
            print("We have a winner", client_socket.getpeername())
            my_lock.acquire()
            winner_client = (client_socket.getpeername()[0], new_port)
            new_clients.append((client_socket.getpeername()[0], new_port))
            my_lock.release()
            print("Thread ", my_id_count, "  winner")
            client_socket.close()
            e.set()

        else:
            client_socket.sendall(struct.pack("!i", -2))
            new_port = struct.unpack("!i", client_socket.recv(4))[0]
            my_lock.acquire()
            new_clients.append((client_socket.getpeername()[0], new_port))
            my_lock.release()
            client_socket.close()
            print("Thread ", my_id_count, "  looser")

    print("Worker Thread ", my_id_count, " end")


if __name__ == '__main__':
    server_socket = None
    try:
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_socket.bind(SERV_ADDR)
        server_socket.listen(5)
        print("Listening...")
    except socket.error as msg:
        print(msg.strerror)
        exit(-1)

    print(f"The number that should be guessed: {my_num} with {number_of_digits} digits")
    t = threading.Thread(target=resetServ, daemon=True)
    t.start()
    while not client_guessed:
        client_socket, client_address = server_socket.accept()
        my_lock.acquire()
        clients.append(client_address)
        client_count += 1
        my_lock.release()
        t = threading.Thread(target=worker, args=(client_socket,))
        threads.append(t)

        t.start()