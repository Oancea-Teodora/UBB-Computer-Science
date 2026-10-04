#include <thread>
#include <mutex>
#include <vector>
#include <iostream>
using namespace std;

void comp_sum(int &sum, int n_threads, int i1, int i2, int j1, int j2, vector<vector<int>> &mat) {
    if (i1 == i2 && j1 == j2) {
        sum = mat[i1][j1];
        return;
    }
    if (n_threads == 1) {
        for (int i=i1;i<=i2;i++) {
            for (int j=j1;j<=j2;j++) {
                sum += mat[i][j];
            }
        }
        return;
    }
    if (i1 == i2) {
        int lsum = 0, rsum = 0;
        int half_threads = n_threads / 2;
        int jmid = (j1 + j2) / 2;
        thread t_left(comp_sum, ref(lsum), half_threads, i1, i2, j1, jmid, ref(mat));
        thread t_right(comp_sum, ref(rsum), n_threads - half_threads, i1, i2, jmid+1, j2, ref(mat));
        t_left.join(); t_right.join();
        sum = lsum + rsum;
    }
    else {
        int lsum = 0, rsum = 0;
        int half_threads = n_threads / 2;
        int imid = (i1 + i2) / 2;
        thread t_left(comp_sum, ref(lsum), half_threads, i1, imid, j1, j2, ref(mat));
        thread t_right(comp_sum, ref(rsum), n_threads - half_threads, imid+1, i2, j1, j2, ref(mat));
        t_left.join(); t_right.join();
        sum = lsum + rsum;
    }
}

int main() {
    int n_threads = 3;
    vector<vector<int>> mat = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
    int n = mat.size(), m = mat[0].size(), sum = 0;
    comp_sum(ref(sum), n_threads, 0, n - 1, 0, m - 1, ref(mat));
    cout << sum << '\n';
    return 0;
}