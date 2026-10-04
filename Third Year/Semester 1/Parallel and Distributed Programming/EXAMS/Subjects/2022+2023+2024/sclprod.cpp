#include <mutex>
#include <iostream>
#include <vector>
#include <thread>
using namespace std;

void scalar_prod(int &sum, int n_threads, int l, int r, vector<int> &a, vector<int> &b) {
    if (l == r) {
        sum = a[l] * b[l];
        return;
    }
    if (n_threads == 1) {
        for (int i=l;i<=r;i++) {
            sum += a[i] * b[i];
        }
        return;
    }
    int lsum = 0, rsum = 0, half_threads = n_threads / 2, mid = (l + r) / 2;
    thread t_left(scalar_prod, ref(lsum), half_threads, l, mid, ref(a), ref(b));
    thread t_right(scalar_prod, ref(rsum), n_threads - half_threads, mid+1, r, ref(a), ref(b));
    t_left.join();
    t_right.join();
    sum = lsum + rsum;
}

int main(int argc, char *argv[]) {
    vector<int> a = {1, 4, 8, 1, 2, 3, 4};
    vector<int> b = {2, 4, 0, 8, 1, 9, 9};
    // 2*1 + 4*4 + 8*0 + 1*8 + 2*1 + 3*9 + 4*9 = 91
    int n_threads = atoi(argv[1]);
    cout << n_threads << '\n';
    int sum = 0, n = a.size();
    thread t(scalar_prod, ref(sum), n_threads, 0, n - 1, ref(a), ref(b));
    t.join();
    cout << sum << '\n';
}