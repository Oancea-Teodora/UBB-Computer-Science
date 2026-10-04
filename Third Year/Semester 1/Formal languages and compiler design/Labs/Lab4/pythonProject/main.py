
from collections import defaultdict

EPS = "ε"

def make_dfa_identifiers():
    letters = [chr(c) for c in range(ord('A'), ord('Z')+1)] + [chr(c) for c in range(ord('a'), ord('z')+1)]
    digits  = [str(d) for d in range(10)]
    alpha   = set(letters + digits + ['_'])
    states = {"q0","q1","qdead"}
    start, finals = "q0", {"q1"}
    delta = {}
    for a in alpha:
        delta[("q0", a)] = "q1" if a in letters else "qdead"
        if a in letters or a in digits or a == "_":
            delta[("q1", a)] = "q1"
        delta[("qdead", a)] = "qdead"
    return dict(states=states, alphabet=alpha, delta=delta, start=start, finals=finals)

def make_dfa_numbers():
    digits = [str(d) for d in range(10)]
    alpha  = set(digits + ['.'])
    states = {"q0","qI","qDot","qF","qdead"}
    start, finals = "q0", {"qI","qF"}
    d = {}
    for a in digits + ['.']:
        d[("qdead", a)] = "qdead"
    for a in digits:
        d[("q0", a)] = "qI"
        d[("qI", a)] = "qI"
        d[("qF", a)] = "qF"
    d[("qI", ".")] = "qDot"
    for a in digits:
        d[("qDot", a)] = "qF"
    return dict(states=states, alphabet=alpha, delta=d, start=start, finals=finals)

def dfa_to_rg(DFA, drop_dead=True):
    states = set(DFA["states"])
    if drop_dead and "qdead" in states:
        states.remove("qdead")
    P = defaultdict(list)
    for (p, a), q in DFA["delta"].items():
        if p in states and q in states:
            P[p].append((a, q))
    for f in DFA["finals"]:
        if f in states:
            P[f].append((EPS, None))
    return dict(N=states, Sigma=DFA["alphabet"], P=P, S=DFA["start"])

def print_rg(RG, title):
    print(f"\n=== {title} ===")
    print(f"S = {RG['S']}")
    for A in sorted(RG["N"]):
        rules = RG["P"].get(A, [])
        if not rules:
            continue
        alts = []
        for a, B in rules:
            if a == EPS:
                alts.append(EPS)
            elif B is None:
                alts.append(a)
            else:
                alts.append(f"{a} {B}")
        print(f"{A} → " + " | ".join(alts))

if __name__ == "__main__":
    dfa_id  = make_dfa_identifiers()
    dfa_num = make_dfa_numbers()

    rg_id  = dfa_to_rg(dfa_id)
    rg_num = dfa_to_rg(dfa_num)

    print_rg(rg_id,  "RG from DFA (Identifiers)")
    print_rg(rg_num, "RG from DFA (Numbers)")
