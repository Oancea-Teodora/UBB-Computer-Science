__author__ = 'dadi'

import socket
import threading
import random
import struct
from threading import Timer
timeout_duration = 10
random.seed()
start = 1.0
stop = 1000.0
my_num = random.uniform(start, stop)  # Server number is a random float
print('Server number: ', my_num)
mylock = threading.Lock()
client_guessed = False
best_guess = None
best_client = None
client_count = 0
threads = []

# list of tuples (selected number, client socket, client id) that stores the data send by each client
client_data = []
timer = None  # Timer to track inactivity

def worker(cs):
    global mylock, my_num, client_data, client_count

    my_idcount = client_count
    print('client #', client_count, 'from: ', cs.getpeername(), cs)
    message = 'Hello client #' + str(client_count) + ' ! You are entering the number guess competition now!'
    cs.sendall(bytes(message, 'ascii'))
    # we receive a single number from a client, so remove the while
    try:
        cnumber = cs.recv(8)  # Receive 8 bytes for a real number (double)
        cnumber = struct.unpack('!d', cnumber)[0]  # Unpack it as a double (float)

        print(f"Client #{client_count} selected: {cnumber}")

        # Add client guess and details to client_data (critical code section, protect by mutex)
        mylock.acquire()
        client_data.append((cnumber, cs, my_idcount))
        mylock.release()

    except socket.error as msg:
        print('Error:', msg.strerror)

    # the thread will end now, no need to keep it running
    print(f"Worker Thread {my_idcount} end")


def process_best_guess():
    global mylock, my_num, client_data, best_guess, best_client

    if len(client_data):
        # get the winner
        best_guess, best_client, best_id = min(client_data, key=lambda x: abs(my_num - x[0]))
        error = abs(my_num - best_guess)

        # Notify the winner
        best_client.sendall(bytes(f'You have the best guess with an error of {error:.4f}', 'ascii'))
        print(f'Client #{best_id} has the best guess with an error of {error:.4f}.')

        # Notify other clients
        for _, cs, cid in client_data:
            if cs != best_client:
                cs.sendall(b'You lost!')

    # Close all client connections
    for _, cs, _ in client_data:
        cs.close()

    print("All threads are finished now")
    mylock.acquire()
    reset_server_state()
    mylock.release()


def reset_server_state():
    global client_data, client_count, my_num, threads
    threads = []
    client_data = []
    client_count = 0
    my_num = random.uniform(start, stop)
    print('Server number: ', my_num)


def reset_timer():
    global timer
    # in python, you cannot restart an existing timer
    # so we cancel the timer and then create another timer object
    if timer is not None:
        timer.cancel()
    timer = Timer(timeout_duration, process_best_guess)
    timer.start()


if __name__ == '__main__':
    try:
        rs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        rs.bind(('0.0.0.0', 1235))
        rs.listen(5)
    except socket.error as msg:
        print(msg.strerror)
        exit(-1)

    while True:
        client_socket, addrc = rs.accept()
        # reset timer as we got a new connection. you cannot directly reset it in python, so we made a function for this
        reset_timer()
        t = threading.Thread(target=worker, args=(client_socket,))
        threads.append(t)
        client_count += 1
        t.start()
