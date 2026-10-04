#include <iostream>
#include <vector>
#include <thread>
#include <chrono>
#include <mutex>

using namespace std;

int n = 500;
int threads = 4;
string mode = "cols";

vector<int> a, b, c;
mutex print_mtx;

int idx(int row, int col) { return row * n + col; }

int calculate_element(int i, int j, int tid) {
    int s = 0;
    for (int k = 0; k < n; k++) s += a[idx(i, k)] * b[idx(k, j)];
    {
        lock_guard<mutex> lock(print_mtx);
        cout << "compute c[" << i << "," << j << "] by thread " << tid << "\n";
    }
    return s;
}

void computeAfterRows(int tid) {
    int total = n * n;
    int baza = total / threads;
    int extra = total % threads;
    int start = tid * baza + min(tid, extra);
    int count = baza + (tid < extra ? 1 : 0);
    int end = start + count;
    for (int lin = start; lin < end; lin++) {
        int i = lin / n;
        int j = lin % n;
        c[lin] = calculate_element(i, j, tid);
    }
}

void computeAfterCols(int tid) {
    int total = n * n;
    int baza = total / threads;
    int extra = total % threads;
    int start = tid * baza + min(tid, extra);
    int count = baza + (tid < extra ? 1 : 0);
    int end = start + count;
    for (int lin = start; lin < end; lin++) {
        int j = lin / n;
        int i = lin % n;
        c[idx(i, j)] = calculate_element(i, j, tid);
    }
}

void computerCyclic(int tid) {
    for (int lin = tid; lin < n*n; lin += threads) {
        int i = lin / n;
        int j = lin % n;
        c[idx(i, j)] = calculate_element(i, j, tid);
    }
}

int main() {
    a.assign(n * n, 0);
    b.assign(n * n, 0);
    c.assign(n * n, 0);
    for (int i = 0; i < n; i++)
        for (int k = 0; k < n; k++)
            a[idx(i, k)] = (i + k) % 10;
    for (int k = 0; k < n; k++)
        for (int j = 0; j < n; j++)
            b[idx(k, j)] = (k + j) % 10;

    vector<thread> th;
    auto t0 = chrono::high_resolution_clock::now();

    if (mode == "rows") {
        for (int t = 0; t < threads; t++) th.push_back(thread(computeAfterRows, t));
    } else if (mode == "cols") {
        for (int t = 0; t < threads; t++) th.push_back(thread(computeAfterCols, t));
    } else {
        for (int t = 0; t < threads; t++) th.push_back(thread(computerCyclic, t));
    }

    for (auto & t : th) t.join();

    auto t1 = chrono::high_resolution_clock::now();
    double ms = chrono::duration<double, std::milli>(t1 - t0).count();
    cout << "Time: " << ms << " ms\n";

    if (n <= 9) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) cout << c[idx(i, j)] << (j + 1 == n ? '\n' : ' ');
        }
    }
    return 0;
}
