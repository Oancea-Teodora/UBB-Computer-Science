
import re
import socket
import threading
import time
import sys
import os

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
broadcastAddress = '255.255.255.255'
port = 7777
timeQueryString = b"TIMEQUERY\0"
dateQueryString = b"DATEQUERY\0"
s.bind(('0.0.0.0', port))
peers_dict = dict() #key-IPAdress and an tuple formed out of an
# integer(count of the last 3 broadcasts) and a String-last response
malformed_list = []
response_count = 3
lock = threading.Lock()


def sendDateQuery():
    global s, port, timeQueryString, dateQueryString, broadcastAddress
    msg = dateQueryString
    while True:
        b = s.sendto(msg, (broadcastAddress, port))
        if b != len(msg):
            print("no date query was sent")
        updatePeers()
        time.sleep(10)


def updatePeers():
    global peers_dict,lock
    lock.acquire()
    new_peers_dict = dict()
    for peer in peers_dict:
        new_value = (peers_dict[peer][0], peers_dict[peer][1] - 1)
        if new_value[1] > 0:
            new_peers_dict[peer] = new_value
    peers_dict = new_peers_dict
    lock.release()




def sendTimeQuery():
    global s, port, timeQueryString, dateQueryString, broadcastAddress
    msg = timeQueryString
    while True:
        b = s.sendto(msg, (broadcastAddress, port))
        if b != len(msg):
            print("no time query was sent")
        updatePeers()
        time.sleep(3)



def regexMatch(msg, pattern):
    pattern = re.compile(pattern)
    return pattern.fullmatch(msg)

def display():
    global malformed_list, peers_dict, lock
    while True:
        os.system("cls")
        print("peers are: ")
        lock.acquire()
        for peer in peers_dict:
            print(peer, peers_dict[peer][0])
        print("malformed data is: ")
        for mal in malformed_list:
            print(mal)
        lock.release()
        time.sleep(2)

def respondQuery():
    global s, port, timeQueryString, dateQueryString, peers_dict, lock, malformed_list
    while True:
        msg, addr = s.recvfrom(1024)
        if msg == timeQueryString:
            myTime = time.strftime("TIME %H:%M:%S")
            #print(f"responding with {myTime} to ip: {addr[0]}, port {addr[1]}")
            myTime = myTime.encode()
            b = s.sendto(myTime, addr)
            if b != len(myTime):
                print("my time not sent")
        elif msg == dateQueryString:
            myDate = time.strftime("DATE %d:%m:%Y")
            #print(f"responding with {myDate} to ip: {addr[0]}, port {addr[1]}")
            myDate = myDate.encode()
            b = s.sendto(myDate, addr)
            if b != len(myDate):
                print("my date not sent")
        else:
            msg = msg.decode()
            lock.acquire()
            if regexMatch(msg=msg, pattern="TIME [0-9]{2}:[0-9]{2}:[0-9]{2}") or regexMatch(msg=msg ,pattern = "DATE [0-9]{2}:[0-9]{2}:[0-9]{4}"):
                peers_dict[addr[0]] = (msg, response_count)
            else :
                if len(malformed_list) > 10:
                    malformed_list.pop()
                malformed_list.insert(0, (addr, msg))
            lock.release()




            # TODO for date regex

            # TODO peers dict and removing part
            # if addr[0] not in peersList.keys():
            #   peersList[addr[0]] = (msg.decode(), 3)


if __name__ == '__main__':
    print("Running...")
    args = sys.argv
    if len(args) <= 1:
        print("specify the broadcast address")
        exit(-1)
    broadcastAddress = args[1]
    threads = []
    t1 = threading.Thread(target=sendTimeQuery)
    t2 = threading.Thread(target=sendDateQuery)
    t3 = threading.Thread(target=display)
    threads.append(t1)
    threads.append(t2)
    threads.append(t3)
    t1.start()
    t2.start()
    t3.start()
    respondQuery()
    for t in threads:
        t.join()
