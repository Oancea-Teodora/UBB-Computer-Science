#include <iostream>
#include <thread>
#include <mutex>
using namespace std;

struct Node {
    int v;
    Node *prev, *next;
    mutex m;
    Node(int x=0): v(x), prev(nullptr), next(nullptr) {}
};

struct DList {
    Node head, tail;
    DList() { head.next = &tail; tail.prev = &head; }

    Node* move_next(Node* cur) {
        if (!cur) return nullptr;
        lock_guard<mutex> g(cur->m);
        return cur->next;
    }
    Node* move_prev(Node* cur) {
        if (!cur) return nullptr;
        lock_guard<mutex> g(cur->m);
        return cur->prev;
    }

    Node* insert_after(Node* cur, int val) {
        for (;;) {
            cur->m.lock();
            Node* nxt = cur->next;
            if (!nxt) { cur->m.unlock(); return nullptr; }
            if (nxt->m.try_lock()) {
                if (cur->next != nxt || nxt->prev != cur) { nxt->m.unlock(); cur->m.unlock(); continue; }

                Node* n = new Node(val);
                n->prev = cur; n->next = nxt;
                cur->next = n; nxt->prev = n;
                nxt->m.unlock(); cur->m.unlock();
                return n;
            } else {
                cur->m.unlock();
            }
        }
    }

    Node* insert_before(Node* cur, int val) {
        for (;;) {
            cur->m.lock();
            Node* prv = cur->prev;
            if (!prv) { cur->m.unlock(); return nullptr; }
            if (prv->m.try_lock()) {
                if (cur->prev != prv || prv->next != cur) { prv->m.unlock(); cur->m.unlock(); continue; }
                Node* n = new Node(val);
                n->next = cur; n->prev = prv;
                prv->next = n; cur->prev = n;
                prv->m.unlock(); cur->m.unlock();
                return n;
            } else {
                cur->m.unlock();
            }
        }
    }

    void print() {
        Node* p = &head;
        cout << "HEAD";
        while (true) {
            Node* n = p->next;
            if (n == &tail) break;
            cout << " - " << n->v;
            p = n;
        }
        cout << " - TAIL\n";
    }
};

int main() {
    DList L;
    Node* n1 = L.insert_after(&L.head, 1);
    Node* n2 = L.insert_after(n1, 2);
    Node* n3 = L.insert_after(n2, 3);

    thread A([&]{
        Node* cur = n1;
        for (int i = 10; i < 15; ++i) {
            if (cur == &L.tail) cur = L.insert_before(cur, i);
            else cur = L.insert_after(cur, i);
            cur = L.move_next(cur);
        }
    });

    thread B([&]{
        Node* cur = n3;
        for (int i = 20; i < 25; ++i) {
            if (cur == &L.head) cur = L.insert_after(cur, i);
            else cur = L.insert_before(cur, i);
            cur = L.move_prev(cur);
        }
    });

    A.join(); B.join();

    L.print();
    return 0;
}
