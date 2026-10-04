import 'package:flutter/material.dart';

import '../models/record.dart';
import '../services/local_db.dart';
import '../services/api_service.dart';
import '../services/call_gate.dart';

// =================================================================
// REPOSITORY - Manages data flow (offline-first + gate rules)
// =================================================================

class RecordsRepository extends ChangeNotifier {
  final LocalDb db;
  final ApiService api;
  final CallGate gate;

  RecordsRepository({required this.db, required this.api, required this.gate});

  bool offline = false; // true if last request failed due to network
  List<Record> listCache = []; // main list cache
  final Map<int, Record> detailsCache = {}; // details cache by id
  List<Record> allCache = []; // all records cache (for reports/insights)

  void _setOffline(bool value) {
    offline = value;
    notifyListeners();
  }

  // Called when user presses Retry after being offline
  void resetAfterOfflineRetry() {
    gate.resetAll();
    _setOffline(false);
  }

  // Load main list (with gate check)
  Future<void> loadListIfNeeded({bool allowNetwork = true}) async {
    // If already in memory, don't reload
    if (listCache.isNotEmpty) return;

    // Load from local DB first (instant offline support)
    listCache = await db.getAll();
    notifyListeners();

    if (!allowNetwork) return;
    if (!gate.canFetchList()) return;

    try {
      final remote = await api.getList();
      await db.upsertAll(remote);
      listCache = remote;
      gate.markListFetched();
      _setOffline(false);
    } on OfflineException catch (e) {
      print("[OFFLINE] $e");
      _setOffline(true);
    }
  }

  // Force reload after retry (resets gate first)
  Future<void> forceReloadListAfterRetry() async {
    resetAfterOfflineRetry();

    listCache = await db.getAll();
    notifyListeners();

    try {
      final remote = await api.getList();
      await db.upsertAll(remote);
      listCache = remote;
      gate.markListFetched();
      _setOffline(false);
    } on OfflineException catch (e) {
      print("[OFFLINE] $e");
      _setOffline(true);
    }
  }

  // Load details for one record (with gate check per id)
  Future<void> loadDetailsIfNeeded(int id, {bool allowNetwork = true}) async {
    // Load from local DB first
    final local = await db.getById(id);
    if (local != null) {
      detailsCache[id] = local;
      notifyListeners();
    }

    if (!allowNetwork) return;
    if (!gate.canFetchDetails(id)) return;

    try {
      final remote = await api.getById(id);
      await db.upsertOne(remote);
      detailsCache[id] = remote;
      gate.markDetailsFetched(id);
      _setOffline(false);
      notifyListeners();
    } on OfflineException catch (e) {
      print("[OFFLINE] $e");
      _setOffline(true);
    }
  }

  // Force reload details after retry
  Future<void> forceReloadDetailsAfterRetry(int id) async {
    resetAfterOfflineRetry();
    await loadDetailsIfNeeded(id, allowNetwork: true);
  }

  // Add new record (online only)
  Future<void> addOnlineOnly({
    required String date,
    required double amount,
    required String type,
    required String category,
    required String description,
  }) async {
    try {
      final created = await api.create(
        date: date,
        amount: amount,
        type: type,
        category: category,
        description: description,
      );
      await db.upsertOne(created);

      // Update caches
      _upsertInList(created);
      _upsertInAll(created);

      notifyListeners();
      _setOffline(false);
    } on OfflineException catch (e) {
      print("[OFFLINE] $e");
      _setOffline(true);
      rethrow;
    }
  }

  // Delete record (online only)
  Future<void> deleteOnlineOnly(int id) async {
    try {
      final deleted = await api.delete(id);
      await db.deleteById(deleted.id);

      // Remove from caches
      listCache.removeWhere((r) => r.id == id);
      allCache.removeWhere((r) => r.id == id);
      detailsCache.remove(id);

      notifyListeners();
      _setOffline(false);
    } on OfflineException catch (e) {
      print("[OFFLINE] $e");
      _setOffline(true);
      rethrow;
    }
  }

  // Load all records for reports/insights (with gate check)
  Future<void> loadAllIfNeeded({bool allowNetwork = true}) async {
    if (allCache.isNotEmpty) return;

    // Load from local DB first
    allCache = await db.getAll();
    notifyListeners();

    if (!allowNetwork) return;
    if (!gate.canFetchAll()) return;

    try {
      final remote = await api.getAll();
      await db.upsertAll(remote);
      allCache = remote;
      gate.markAllFetched();
      _setOffline(false);
      notifyListeners();
    } on OfflineException catch (e) {
      print("[OFFLINE] $e");
      _setOffline(true);
    }
  }

  // Force reload all after retry
  Future<void> forceReloadAllAfterRetry() async {
    resetAfterOfflineRetry();

    allCache = await db.getAll();
    notifyListeners();

    try {
      final remote = await api.getAll();
      await db.upsertAll(remote);
      allCache = remote;
      gate.markAllFetched();
      _setOffline(false);
      notifyListeners();
    } on OfflineException catch (e) {
      print("[OFFLINE] $e");
      _setOffline(true);
    }
  }

  // Handle WebSocket push (save locally + update UI)
  Future<void> onWsRecord(Record r) async {
    print("[WS] Received id=${r.id}");
    await db.upsertOne(r);
    _upsertInList(r);
    _upsertInAll(r);
    notifyListeners();
  }

  void _upsertInList(Record r) {
    final idx = listCache.indexWhere((x) => x.id == r.id);
    if (idx == -1) {
      listCache.insert(0, r);
    } else {
      listCache[idx] = r;
    }
  }

  void _upsertInAll(Record r) {
    final idx = allCache.indexWhere((x) => x.id == r.id);
    if (idx == -1) {
      allCache.insert(0, r);
    } else {
      allCache[idx] = r;
    }
  }

  // REPORTS: Monthly totals (sorted descending by amount)
  List<MapEntry<String, double>> monthlyTotals() {
    final map = <String, double>{};
    for (final r in allCache) {
      final month = (r.date.length >= 7) ? r.date.substring(0, 7) : "unknown";
      map[month] = (map[month] ?? 0) + r.amount;
    }
    final entries = map.entries.toList();
    entries.sort((a, b) => b.value.compareTo(a.value));
    return entries;
  }

  // INSIGHTS: Top 3 categories by spending
  List<MapEntry<String, double>> top3Categories() {
    final map = <String, double>{};
    for (final r in allCache) {
      final cat = r.category.isEmpty ? "unknown" : r.category;
      map[cat] = (map[cat] ?? 0) + r.amount;
    }
    final entries = map.entries.toList();
    entries.sort((a, b) => b.value.compareTo(a.value));
    return entries.take(3).toList();
  }
}
