import socket, threading, json, sys, time, heapq

N = 3
HOST = "127.0.0.1"
BASE_PORT = 5000

VARS = ["A", "B", "C"]
SUBS = {
    "A": [0, 1],
    "B": [0, 1, 2],
    "C": [0, 2],
}

OWNER = {"A": 0, "B": 1, "C": 2}

SEND_LOCK = threading.Lock()
def send(sock, msg):
    data = (json.dumps(msg) + "\n").encode()
    with SEND_LOCK:
        sock.sendall(data)

class DSM:
    def __init__(self, pid, on_change):
        self.pid = pid
        self.cb = on_change

        self.owned = [v for v in VARS if OWNER[v] == pid]
        self.own_val = {v: 0 for v in self.owned}
        self.view = {v: 0 for v in VARS}

        self.lock = threading.RLock()
        self.clock = 0
        self.last_seen = {i: 0 for i in range(N)}
        self.rel_owners = {OWNER[v] for v in VARS if pid in SUBS[v]}

        self.hold = []
        self.cb_idx = 0

        self.peers = {}

        self.req_lock = threading.Lock()
        self.rid = 0
        self.wait = {}
        self.ans = {}

        self._net_start()
        threading.Thread(target=self._hb_loop, daemon=True).start()

    def _ts_send(self):
        with self.lock:
            self.clock += 1
            self.last_seen[self.pid] = max(self.last_seen[self.pid], self.clock)
            return self.clock

    def _ts_recv(self, ts):
        with self.lock:
            self.clock = max(self.clock, ts) + 1
            self.last_seen[self.pid] = max(self.last_seen[self.pid], self.clock)

    def _net_start(self):
        srv = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        srv.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        srv.bind((HOST, BASE_PORT + self.pid))
        srv.listen()

        def recv_thread(f):
            for line in f:
                m = json.loads(line)
                t = m["t"]

                if t == "REQ":
                    self._handle_req(m)

                elif t == "RESP":
                    rid = m["id"]
                    with self.req_lock:
                        self.ans[rid] = m["ok"]
                        ev = self.wait.get(rid)
                    if ev:
                        ev.set()

                elif t == "UPD":
                    self._on_upd(m)

                elif t == "HB":
                    self._ts_recv(m["ts"])
                    with self.lock:
                        self.last_seen[m["f"]] = max(self.last_seen[m["f"]], m["ts"])
                    self._deliver()

        def accept_loop():
            while len(self.peers) < N - 1:
                c, _ = srv.accept()
                f = c.makefile("r")
                other = json.loads(f.readline())["pid"]
                self.peers[other] = c
                threading.Thread(target=recv_thread, args=(f,), daemon=True).start()

        threading.Thread(target=accept_loop, daemon=True).start()

        for other in range(self.pid + 1, N):
            while True:
                try:
                    c = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                    c.connect((HOST, BASE_PORT + other))
                    send(c, {"pid": self.pid})
                    self.peers[other] = c
                    f = c.makefile("r")
                    threading.Thread(target=recv_thread, args=(f,), daemon=True).start()
                    break
                except:
                    time.sleep(0.2)

        while len(self.peers) < N - 1:
            time.sleep(0.05)

    def _handle_req(self, m):
        var = m["var"]
        frm = m["f"]
        rid = m["id"]

        if OWNER[var] != self.pid:
            return

        ok = False
        changed = False

        with self.lock:
            self._ts_recv(m["ts"])

            if frm not in SUBS[var]:
                ok = False

            elif m["op"] == "W":
                ok = True
                nv = int(m["val"])
                if self.own_val.get(var, 0) != nv:
                    self.own_val[var] = nv
                    changed = True

            else:
                exp = int(m["exp"])
                nv = int(m["new"])
                if self.own_val.get(var, 0) == exp:
                    self.own_val[var] = nv
                    ok = True
                    changed = True
                else:
                    ok = False

            if changed:
                ts = self._ts_send()
                upd = {"t": "UPD", "var": var, "val": self.own_val[var], "ts": ts, "own": self.pid, "op": m["op"]}
                for p in SUBS[var]:
                    if p == self.pid:
                        self._on_upd(upd, local_owner=True)
                    else:
                        send(self.peers[p], upd)

        if frm != self.pid:
            send(self.peers[frm], {"t": "RESP", "id": rid, "ok": ok})

    def _on_upd(self, m, local_owner=False):
        if not local_owner:
            self._ts_recv(m["ts"])

        with self.lock:
            self.last_seen[m["own"]] = max(self.last_seen[m["own"]], m["ts"])
            heapq.heappush(self.hold, (m["ts"], m["own"], m["var"], m["val"], m["op"]))

        self._deliver()

    def _deliver(self):
        while True:
            with self.lock:
                if not self.hold:
                    return
                ts, own, var, val, op = self.hold[0]

                for o in self.rel_owners:
                    if self.last_seen.get(o, 0) < ts:
                        return

                heapq.heappop(self.hold)
                self.view[var] = val
                self.cb_idx += 1
                idx = self.cb_idx

            self.cb(idx, var, val, own, op, ts)

    def _hb_loop(self):
        targets = set()
        for v in self.owned:
            for p in SUBS[v]:
                if p != self.pid:
                    targets.add(p)
        targets = list(targets)

        while True:
            time.sleep(0.3)
            if not targets:
                continue
            ts = self._ts_send()
            hb = {"t": "HB", "f": self.pid, "ts": ts}
            for p in targets:
                send(self.peers[p], hb)

    def _rpc(self, owner_pid, req):
        with self.req_lock:
            self.rid += 1
            rid = self.rid
            ev = threading.Event()
            self.wait[rid] = ev
        req["id"] = rid
        send(self.peers[owner_pid], req)
        ev.wait()
        with self.req_lock:
            ok = self.ans.get(rid, False)
            self.wait.pop(rid, None)
        return ok

    def write(self, var, value):
        if self.pid not in SUBS.get(var, []):
            return False
        ts = self._ts_send()
        o = OWNER[var]
        if o == self.pid:
            self._handle_req({"t": "REQ", "f": self.pid, "id": 0, "ts": ts, "op": "W", "var": var, "val": int(value)})
            return True
        return self._rpc(o, {"t": "REQ", "f": self.pid, "ts": ts, "op": "W", "var": var, "val": int(value)})

    def cas(self, var, expected, new_value):
        if self.pid not in SUBS.get(var, []):
            return False
        ts = self._ts_send()
        o = OWNER[var]
        if o == self.pid:
            before = self.own_val.get(var, 0)
            self._handle_req({"t": "REQ", "f": self.pid, "id": 0, "ts": ts, "op": "C",
                              "var": var, "exp": int(expected), "new": int(new_value)})
            return before == int(expected)
        return self._rpc(o, {"t": "REQ", "f": self.pid, "ts": ts, "op": "C",
                             "var": var, "exp": int(expected), "new": int(new_value)})

def main():
    pid = int(sys.argv[1])

    def on_change(i, var, val, owner, op, ts):
        print(f"[P{pid}] cb#{i} {'WRITE' if op=='W' else 'CAS'} {var}={val} (owner P{owner}, ts={ts})")

    dsm = DSM(pid, on_change)

    print(f"P{pid} ready. Subscribed: {[v for v in VARS if pid in SUBS[v]]}")
    print("Commands: w VAR VALUE | cas VAR EXPECTED NEW | read VAR")

    while True:
        s = input("> ").strip().split()
        if not s:
            continue
        if s[0] == "w" and len(s) == 3:
            print("ok" if dsm.write(s[1], s[2]) else "denied")
        elif s[0] == "cas" and len(s) == 4:
            print("ok" if dsm.cas(s[1], s[2], s[3]) else "fail/denied")
        elif s[0] == "read" and len(s) == 2:
            print(dsm.view.get(s[1], "?"))
        else:
            print("bad command")

if __name__ == "__main__":
    main()
