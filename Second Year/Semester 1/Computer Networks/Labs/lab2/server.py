__author__ = 'dadi'

import socket
import threading
import random
import struct

random.seed()
timeout_duration = 10
start = 1.0
stop = 1000.0
my_num = random.uniform(start, stop)
print('Server number: ', my_num)
mylock = threading.Lock()
client_guessed = False
winner_thread = 0
best_guess = None
best_client = None
client_count = 0
e = threading.Event()
e.clear()
threads = []
# client_data stores info in the format (selected number, client socket, client id)
client_data = []
def worker(cs):
    global mylock, client_guessed, my_num, winner_thread, best_guess, best_client, client_data, client_count

    my_idcount = client_count
    print('client #', client_count, 'from: ', cs.getpeername(), cs)
    message = 'Hello client #' + str(client_count) + ' ! You are entering the number guess competition now!'
    cs.sendall(bytes(message, 'ascii'))

    try:
        # we no longer have a while loop as the client send a single number
        # the client sends a real number on double precision, so we need to receive 8 bytes
        cnumber = cs.recv(8)
        cnumber = struct.unpack('!d', cnumber)[0]  # Unpack it as a double
        print(f"Client #{client_count} guessed: {cnumber}")

        mylock.acquire()
        client_data.append((cnumber, cs, my_idcount))
        mylock.release()

    except socket.error as msg:
        print('Error:', msg.strerror)

    print(f"Worker Thread {my_idcount} end")


def resetSrv():
    global mylock, client_guessed, winner_thread, my_num, threads, e, client_count, best_guess, best_client, client_data
    while True:
        e.wait()  # Wait for event to be set after timeout
        if client_data:
            # get the winner
            best_guess, best_client, best_id = min(client_data, key=lambda x: abs(my_num - x[0]))
            error = abs(my_num - best_guess)

            # Notify the winner
            best_client.sendall(bytes(f'You have the best guess with an error of {error:.4f}', 'ascii'))
            print(f'Client #{best_id} has the best guess with an error of {error:.4f}.')

            # Notify the losers
            for _, cs, cid in client_data:
                if cs != best_client:
                    cs.sendall(b'You lost!')

        # don't forget to close connections
        for _, cs, _ in client_data:
            cs.close()

        print("All sockets closed ")
        e.clear()
        mylock.acquire()
        threads = []
        client_data = []
        client_guessed = False
        winner_thread = -1
        client_count = 0
        my_num = random.uniform(start, stop)
        print('Server number: ', my_num)
        mylock.release()


if __name__ == '__main__':
    try:
        rs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        rs.bind(('0.0.0.0', 1235))
        rs.listen(5)
        # Set 10 seconds timeout for inactivity on rendez-vous socket
        rs.settimeout(timeout_duration)
    except socket.error as msg:
        print(msg.strerror)
        exit(-1)

    t = threading.Thread(target=resetSrv, daemon=True)
    t.start()

    while True:

        try:
            client_socket, addrc = rs.accept()
            t = threading.Thread(target=worker, args=(client_socket,))
            threads.append(t)
            client_count += 1
            t.start()
        except socket.timeout: # in case of inactivity, this exception is thrown
            print("No connections for 10 seconds, choosing the best guess.")
            e.set()  # reset server
