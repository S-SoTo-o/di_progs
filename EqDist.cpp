#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

vector<int> findEquidistant(vector<vector<int>>& graph, vector<int>& supports) {
    int n = graph.size();
    int k = supports.size();
    vector<int> result;

    if (k == 0) {
        for (int i = 0; i < n; i++) {
            result.push_back(i);
        }
        return result;
    }

    vector<vector<int>> dists(k, vector<int>(n, -1));

    for (int i = 0; i < k; i++) {
        queue<int> q;
        q.push(supports[i]);
        dists[i][supports[i]] = 0;

        while (!q.empty()) {
            int u = q.front();
            q.pop();

            for (int v : graph[u]) {
                if (dists[i][v] == -1) {
                    dists[i][v] = dists[i][u] + 1;
                    q.push(v);
                }
            }
        }
    }

    for (int u = 0; u < n; u++) {
        bool equidistant = true;
        int firstDist = dists[0][u];

        if (firstDist == -1) {
            continue;
        }

        for (int i = 1; i < k; i++) {
            if (dists[i][u] != firstDist) {
                equidistant = false;
                break;
            }
        }

        if (equidistant) {
            result.push_back(u);
        }
    }

    return result;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n, m;
    cin >> n >> m;

    vector<vector<int>> graph(n);

    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }

    int k;
    cin >> k;
    vector<int> supports(k);

    for (int i = 0; i < k; i++) {
        cin >> supports[i];
    }

    vector<int> result = findEquidistant(graph, supports);

    if (result.empty()) {
        cout << "-" << endl;
    } else {
        sort(result.begin(), result.end());
        for (int v : result) {
            cout << v << " ";
        }
        cout << endl;
    }

    return 0;
}
