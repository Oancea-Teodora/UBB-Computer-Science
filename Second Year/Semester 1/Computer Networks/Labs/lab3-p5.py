import sys
import socket
import threading
import time
import re
import os

timeQueryString = b"TIMEQUERY\0"
dateQueryString = b"DATEQUERY\0"
broadcastAddr = ""
peers= {}
malformed = []

def timeQuery(sock):
    while True:
        time.sleep(3)
        try:
            nsent = sock.sendto(timeQueryString,(broadcastAddr,7777))
            if nsent != len(timeQueryString):
                print("Error TIMEQUERY")
        except OSError as err:
            print(err.strerror)

def dateQuery(sock):
    while True:
        time.sleep(10)
        try:
            nsent = sock.sendto(dateQueryString, (broadcastAddr,7777))
            if nsent != len(dateQueryString):
                print("Error DATEQUERY")
        except OSError as err:
            print(err.strerror)

def match_datetime(regex, str):
    reg = re.compile(regex)
    result = reg.search(str)
    if result is None:
        return ''
    else:
        return result.group(1)

def worker(sock):
    global peers
    while True:
        peers_new = {}
        try:
            msg,rAddr = sock.recvfrom(20)

            if msg == timeQueryString:
                strSend = time.strftime("TIME %H:%M:%S")+'\0'
                sock.sendto(strSend.encode(), rAddr)
                lock.acquire()
                for key in peers.keys():
                    peers[key][0]-=1
                    if peers[key][0] > 0:
                        peers_new[key] = peers[key]
                peers = peers_new
                lock.release()
                continue

            if msg== dateQueryString:
                strSend = time.strftime("DATE %d:%m:%Y")+'\0'
                sock.sendto(strSend.encode(),rAddr)
                lock.acquire()
                for key in peers.keys():
                    peers[key][0] -= 1
                    if peers[key][0] > 0 :
                        peers_new[key] = peers[key]
                peers = peers_new
                lock.release()
                continue

            # time
            result = match_datetime("TIME ([0-9][0-9]:[0-9][0-9]:[0-9][0-9])\0", msg.decode())
            if result != "":
                lock.acquire()
                if not peers.keys().__contains__(rAddr):
                    value = [3,result,'']
                else:
                    value = peers[rAddr]
                    value[0]=3
                    value[1] = result
                peers[rAddr] = value
                lock.release()
                continue

            # date
            result = match_datetime("DATE ([0-9][0-9]:[0-9][0-9]:[0-9][0-9][0-9][0-9])\0", msg.decode())
            if result != "":
                lock.acquire()
                if not peers.keys().__contains__(rAddr):
                    value = [3, '', result]
                else:
                    value = peers[rAddr]
                    value[0] = 3
                    value[2] = result
                peers[rAddr] = value;
                lock.release()
                continue

            # malformed
            lock.acquire()
            if len(malformed) > 7:
                malformed.remove(malformed[len(malformed)-1])
            malformed.insert(0,rAddr[0]+":"+str(rAddr[1]) + msg.decode() )
            lock.release()

        except OSError as err:
            print(err.strerror)
            lock.release()


def display():
    while True:
        time.sleep(1)
        if os.name=="nt":
            os.system("cls")
        else:
            os.system("clear")
        lock.acquire()
        print("Peers:")
        for key in peers:
            print(key, "--", peers[key])
        print("Malformed:")
        print(malformed)
        lock.release()


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print(sys.argv[0], "<broadcastaddr>")
        exit(1)

    broadcastAddr = sys.argv[1];
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
        sock.bind( ('',7777) )
    except OSError as ex:
        print(ex.strerror)
        exit(2)
    #mutex
    lock = threading.Lock()

    tqThread = threading.Thread(target=timeQuery, args=(sock,))
    dqThread = threading.Thread(target=dateQuery, args=(sock,))
    workerThread = threading.Thread(target=worker, args=(sock,))
    displayThread = threading.Thread(target=display)
    workerThread.start()
    tqThread.start()
    dqThread.start()
    displayThread.start()

