/*
5) Write an UDP broadcast application that serves as client and server at the same time.
   The application is started with the network broadcast address (<NBCAST>) as argument in the command line.

1. Upon launching the application listens on UDP port 7777.
2. Every 3 seconds the application sends a UDP broadcast message to NBCAST port 7777 with the format: TIMEQUERY\0 (string)
3. Whenever the application receives a TIMEQUERY demand it answers to the source IP:
    port with a string message: TIME HH:MM:SS\0 (current time) using unicast.
4. Every 10 seconds the application sends a UDP broadcast message to NBCAST port 7777 with the format:  DATEQUERY\0 (string)
5. Whenever the application receives a DATEQUERY demand it answers to the source IP:port with a string message: 
    DATE DD:MM:YYYY\0 (current date) using unicast.
6. The application will keep a list of peers (that answer to broadcast – IP:portno) and 
    update the information anytime a unicast message is received upon a broadcast.
7. When an entry in a list does not have any answer for 3 consecutive broadcasts it will be removed from the list.
8. The list will be displayed (ip, date, time) on the screen upon each update 
    (using a screen positioning api like conio or by erasing the screen before each update).
9. Every malformed request/response received will be counted and displayed at the end of a screen update.
    You will have a list of malformed messages displayed with their source IP address.
    The list should be limited in size and implemented as a queue.
    Recent messages are added to the head and old messages are moving towards the tail.
*/
#define _WINSOCK_DEPRECATED_NO_WARNINGS
#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#ifdef _WIN32
#include <WinSock2.h>
#pragma comment(lib, "Ws2_32.lib")
#else
// TODO
#endif
#include <thread>
#include <regex>
#include <mutex>
#include <deque>
#include <unordered_map>
#include <chrono>
#include <ctime>
#define PORT 7777
#define MAX_FAILED_ATTEMPTS 3

std::unordered_map<std::string, std::pair<unsigned, std::string>> peers;
std::deque<std::pair<std::string, std::string>> malformedMessages;
std::mutex mutex;

const char* DATE_QUERY = "DATEQUERY";
const char* TIME_QUERY = "TIMEQUERY";
int UDP_socket;

void SendQueries() {
    int secondsPassed = 0;
    sockaddr_in Recv_addr;
    Recv_addr.sin_family = AF_INET;
    Recv_addr.sin_port = htons(PORT);
    Recv_addr.sin_addr.s_addr  = inet_addr("172.30.255.255"); // this is equiv to 255.255.255.255

    while (true) {
        ++secondsPassed;
        if (secondsPassed % 3 == 0) {
            sendto(UDP_socket, TIME_QUERY, strlen(TIME_QUERY) + 1, 0, (sockaddr*)&Recv_addr, sizeof(Recv_addr));
            mutex.lock();
            for (auto& peer : peers) {
                peer.second.first--;
            }
            mutex.unlock();
        }
        if (secondsPassed % 10 == 0) {
            sendto(UDP_socket, DATE_QUERY, strlen(DATE_QUERY) + 1, 0, (sockaddr*)&Recv_addr, sizeof(Recv_addr));
        }
#ifdef _WIN32
        Sleep(1000);
#else
        usleep(1);
#endif
    }
}

void HandleMessages() {
    sockaddr_in Sender_addr;
    int senderLen = sizeof(Sender_addr);
    memset(&Sender_addr, 0, sizeof(Sender_addr));
    char buf[250];
    std::regex timePattern("TIME [0-9]{2}:[0-9]{2}:[0-9]{2}");

    while (true) {
        recvfrom(UDP_socket, buf, 250, 0, (sockaddr*)&Sender_addr, &senderLen);
        std::string peerIP = inet_ntoa(Sender_addr.sin_addr);
        std::string receivedMsg = buf;

        if (receivedMsg == "TIMEQUERY") {
            std::time_t t = std::time(nullptr);
            char mbstr[100];

            if (std::strftime(mbstr, sizeof(mbstr), "TIME %H:%M:%S", std::localtime(&t))) {
                sendto(UDP_socket, mbstr, strlen(mbstr) + 1, 0, (sockaddr*)&Sender_addr, sizeof(Sender_addr));
            }
        }
        else if (std::regex_match(receivedMsg, timePattern)) {
            mutex.lock();
            peers.insert({ peerIP, std::make_pair(MAX_FAILED_ATTEMPTS, receivedMsg) });
            mutex.unlock();
        }

    }
}

void Display() {
    while (true) {
        mutex.lock();
        for (auto& peer : peers) {
            std::cout << peer.first << std::endl;
        }
        mutex.unlock();
        Sleep(2000);
    }
}

int main()
{
#ifdef _WIN32
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) < 0) {
        printf("Error initializing the Windows Sockets LIbrary");
        return -1;
    }
#endif
    UDP_socket = socket(AF_INET, SOCK_DGRAM, 0);
    char enable = '1';
    setsockopt(UDP_socket, SOL_SOCKET, SO_BROADCAST, &enable, sizeof(enable)); // last arg is of type socklen_t (int)

    sockaddr_in sock_addr;
    sock_addr.sin_port = htons(PORT);
    sock_addr.sin_family = AF_INET;
    sock_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(UDP_socket, (sockaddr*)&sock_addr, sizeof(sockaddr_in)) < 0) {
        perror("Bind error:");
        return 1;
    }

    std::thread querySending{ SendQueries };
    std::thread display{ Display };
    HandleMessages();
    querySending.join();
    display.join();
   
    return 0;
}
