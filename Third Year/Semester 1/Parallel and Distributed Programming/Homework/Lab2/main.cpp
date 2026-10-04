#include <iostream>
#include <vector>
#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <chrono>
#include <iomanip>

using namespace std;

struct Shared {
    queue<double> q;
    size_t cap;
    bool done = false;
    mutex m;
    condition_variable not_full, not_empty;
    explicit Shared(size_t c) : cap(c) {}
};
int main() {
    const size_t n = 1000000;
    const size_t capacity = 1;

    cout << "n = " << n << ", queue capacity = " << capacity << "\n";

    vector<double> v1(n), v2(n);
    for (size_t i = 0; i < n; ++i) {
        v1[i] = i;
        v2[i] = i;
    }

    Shared sh(capacity);
    double result = 0.0;

    auto t0 = chrono::high_resolution_clock::now();

    thread producer([&]() {
        for (size_t i = 0; i < n; ++i) {
            double prod = v1[i] * v2[i];
            unique_lock<mutex> lk(sh.m);
            sh.not_full.wait(lk, [&]{ return sh.q.size() < sh.cap; });
            sh.q.push(prod);
            lk.unlock();
            sh.not_empty.notify_one();
        }
        {
            lock_guard<mutex> lk(sh.m);
            sh.done = true;
        }
        sh.not_empty.notify_all();
    });

    thread consumer([&]() {
        double local_sum = 0.0;
        while (true) {
            unique_lock<mutex> lock(sh.m);
            sh.not_empty.wait(lock, [&]{ return !sh.q.empty() || sh.done; });
            if (sh.q.empty() && sh.done) break;
            double x = sh.q.front(); sh.q.pop();
            lock.unlock();
            sh.not_full.notify_one();
            local_sum += x;
        }
        result = local_sum;
    });

    producer.join();
    consumer.join();

    auto t1 = chrono::high_resolution_clock::now();
    double time = chrono::duration<double, milli>(t1 - t0).count();

    cout << fixed << setprecision(6);
    cout << "producer-consumer: " << result << "  [" << time << " ms]\n";
    return 0;
}
