// =================================================================
// CALL GATE - Prevents repeated GET calls (teacher requirement!)
// =================================================================

class CallGate {
  bool listFetched = false; // GET /plural
  bool allFetched = false; // GET /all...
  final Set<int> detailsFetched = {}; // GET /singular/:id

  // Reset all gates after offline + retry
  void resetAll() {
    print("[GATE] Resetting gates after offline+retry");
    listFetched = false;
    allFetched = false;
    detailsFetched.clear();
  }

  bool canFetchList() => !listFetched;
  bool canFetchAll() => !allFetched;
  bool canFetchDetails(int id) => !detailsFetched.contains(id);

  void markListFetched() => listFetched = true;
  void markAllFetched() => allFetched = true;
  void markDetailsFetched(int id) => detailsFetched.add(id);
}
