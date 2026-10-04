#include <iostream>
#include <vector>
#include <thread>
#include <mutex>
#include <chrono>
#include <memory>
using namespace std;

vector<long long> suminEachAccount;
vector<unique_ptr<mutex>> mtx;
long long expectedTotal = 0;
mutex io_mtx;

void transfer(int from, int to, long long transfer_amount, int tid) {

    int a = min(from, to), b = max(from, to);
    long long after_from = -1, after_to = -1;
    bool moved = false;

    {
        lock_guard<mutex> g1(*mtx[a]);
        lock_guard<mutex> g2(*mtx[b]);
        if (suminEachAccount[from] >= transfer_amount) {
            suminEachAccount[from] -= transfer_amount;
            suminEachAccount[to] += transfer_amount;
            after_from = suminEachAccount[from];
            after_to = suminEachAccount[to];
            moved = true;
        }
    }

    lock_guard<mutex> out(io_mtx);
    if (moved) {
        cout << " Transfer "
             << transfer_amount << "  from " << from << " -> to " << to
             << " | after: sum in each account[" << from << "]=" << after_from
             << ", suminEachAccount[" << to << "]=" << after_to << '\n';
    } else {
        cout << "[T" << tid << "] skip (insufficient funds)  from " << from
             << " -> to " << to << '\n';
    }
}

long long total_money() {
    for (auto &p : mtx) p->lock();
    long long s = 0; for (auto x : suminEachAccount) s += x;
    for (int i = (int)mtx.size() - 1; i >= 0; --i) mtx[i]->unlock();
    return s;
}

void worker(int id, int number_of_transfers, int n) {
    const long long transferAmount = 10;
    auto next_check = chrono::steady_clock::now() + chrono::seconds(5);

    for (int i = 0; i < number_of_transfers; ++i) {
        int from = (id + i) % n;
        int to = (from + 1) % n;
        transfer(from, to, transferAmount, id);

        auto now = chrono::steady_clock::now();
        if (now >= next_check) {
            long long sum = total_money();
            lock_guard<mutex> out(io_mtx);
            if (sum == expectedTotal) cout << "check: total OK: " << sum << '\n';
            else cout << "check: It is not correct! total=" << sum
                      << " expected=" << expectedTotal << '\n';
            next_check = now + chrono::seconds(5);
        }
    }
}

int main() {
    int N = 5;
    int T = 3;
    int number_of_transfers = 20;
    long long initial_sum = 100;

    suminEachAccount.assign(N, initial_sum);
    mtx.resize(N);
    for (int i = 0; i < N; ++i) mtx[i] = make_unique<mutex>();
    expectedTotal = N * initial_sum;

    vector<thread> ts;
    for (int i = 0; i < T; ++i) ts.emplace_back(worker, i, number_of_transfers, N);
    for (auto &t : ts) t.join();

    long long final_total = total_money();

    {
        lock_guard<mutex> out(io_mtx);
        cout << "Balances: "; for (auto x : suminEachAccount) cout << x << ' '; cout << '\n';
        cout << "Initial total: " << expectedTotal
             << "\nFinal total: " << final_total << '\n';
        cout << (expectedTotal == final_total ? "Consistency ok\n"
                                              : "Consistency wrong\n");
    }
}