#include <thread>
#include <vector>
#include <iostream>
using namespace std;

void comp_prod(int start, int jump, int n, int m, int k, vector<vector<int>> &a, vector<vector<int>> &b, vector<vector<int>> &res) {
    for (int i = start; i < n*m; i += jump) {
        int x = i / m, y = i % m;
        for (int j = 0; j < k; j++) {
            res[x][y] += a[x][j] * b[j][y];
        }
    }
}

int main() {
    vector<vector<int>> a = {{1, 2, 3, 1}, {2, 3, 1, 8}, {7, 4, 2, 3}};
    vector<vector<int>> b = {{3, 8}, {4, 9}, {0, 2}, {3, 3}};
    vector<vector<int>> res;
    vector<thread> threads;
    int n_res = a.size(), m_res = b[0].size(), k = a[0].size();
    res.resize(n_res, vector<int>(m_res));
    int n_threads = 5;
    for (int i = 0; i < n_threads; i++) {
        threads.push_back(thread(comp_prod, i, n_threads, n_res, m_res, k, ref(a), ref(b), ref(res)));
    }
    for (auto &t:threads) {
        t.join();
    }
    // res = {{14, 35}, {42, 69}, {46, 105}}
    for (auto &v:res) {
        for (auto &x:v) {
            cout << x << " ";
        }
        cout << '\n';
    }
    return 0;
}