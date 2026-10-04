import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart' as p;
import 'package:web_socket_channel/web_socket_channel.dart';
import 'config.dart';

// MODEL
class R {
  final int id;
  final String date, type, category, description;
  final double amount;
  R({
    required this.id,
    required this.date,
    required this.amount,
    required this.type,
    required this.category,
    required this.description,
  });

  factory R.fromJson(Map<String, dynamic> j) => R(
    id: (j['id'] as num).toInt(),
    date: j['date'] ?? '',
    amount: (j['amount'] as num).toDouble(),
    type: j['type'] ?? '',
    category: j['category'] ?? '',
    description: j['description'] ?? '',
  );

  Map<String, dynamic> toMap() => {
    'id': id,
    'date': date,
    'amount': amount,
    'type': type,
    'category': category,
    'description': description,
  };

  factory R.fromMap(Map<String, dynamic> m) => R(
    id: m['id'],
    date: m['date'],
    amount: (m['amount'] as num).toDouble(),
    type: m['type'],
    category: m['category'] ?? '',
    description: m['description'] ?? '',
  );
}

//  DATABASE
class DB {
  Database? _db;
  Future<void> init() async {
    _db = await openDatabase(
      p.join(await getDatabasesPath(), 'app.db'),
      version: 1,
      onCreate: (db, _) => db.execute(
        'CREATE TABLE items(id INTEGER PRIMARY KEY,date TEXT,amount REAL,type TEXT,category TEXT,description TEXT)',
      ),
    );
    print('[DB] opened');
  }

  Future<void> saveAll(List<R> list) async {
    final b = _db!.batch();
    for (var r in list)
      b.insert(
        'items',
        r.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace,
      );
    await b.commit(noResult: true);
    print('[DB] saved ${list.length}');
  }

  Future<void> saveOne(R r) async {
    await _db!.insert(
      'items',
      r.toMap(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
    print('[DB] saved id=${r.id}');
  }

  Future<List<R>> getAll() async =>
      (await _db!.query('items', orderBy: 'date DESC')).map(R.fromMap).toList();

  Future<R?> getOne(int id) async {
    final rows = await _db!.query(
      'items',
      where: 'id=?',
      whereArgs: [id],
      limit: 1,
    );
    return rows.isEmpty ? null : R.fromMap(rows.first);
  }

  Future<void> delete(int id) async {
    await _db!.delete('items', where: 'id=?', whereArgs: [id]);
    print('[DB] deleted $id');
  }
}

//  API
class Offline implements Exception {
  final String msg;
  Offline(this.msg);
}

class API {
  Future<http.Response> _call(Future<http.Response> Function() fn) async {
    try {
      return await fn().timeout(const Duration(seconds: 10));
    } on SocketException catch (e) {
      throw Offline(e.message);
    } on TimeoutException {
      throw Offline('timeout');
    } on http.ClientException catch (e) {
      throw Offline(e.message);
    }
  }

  Uri _url(String path) => Uri.parse('${C.http}$path');

  Future<List<R>> getList() async {
    print('[API] GET ${C.http}${C.listPath}');
    final r = await _call(() => http.get(_url(C.listPath)));
    print('[API] Response: ${r.statusCode}');
    if (r.statusCode != 200) {
      print('[API] Error GET list: ${r.statusCode} ${r.body}');
      throw Exception('${r.statusCode}');
    }
    return (jsonDecode(r.body) as List).map((e) => R.fromJson(e)).toList();
  }

  Future<R> getOne(int id) async {
    print('[API] GET ${C.http}${C.itemPath}/$id');
    final r = await _call(() => http.get(_url('${C.itemPath}/$id')));
    print('[API] Response: ${r.statusCode}');
    if (r.statusCode != 200) {
      print('[API] Error GET one id=$id: ${r.statusCode} ${r.body}');
      throw Exception('${r.statusCode}');
    }
    return R.fromJson(jsonDecode(r.body));
  }

  Future<R> create(
    String date,
    double amount,
    String type,
    String cat,
    String desc,
  ) async {
    print('[API] POST ${C.http}${C.itemPath}');
    final r = await _call(
      () => http.post(
        _url(C.itemPath),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'date': date,
          'amount': amount,
          'type': type,
          'category': cat,
          'description': desc,
        }),
      ),
    );
    print('[API] Response: ${r.statusCode}');
    if (r.statusCode != 201) {
      print('[API] Error POST: ${r.statusCode} ${r.body}');
      throw Exception('${r.statusCode}');
    }
    return R.fromJson(jsonDecode(r.body));
  }

  Future<void> delete(int id) async {
    print('[API] DELETE ${C.http}${C.itemPath}/$id');
    final r = await _call(() => http.delete(_url('${C.itemPath}/$id')));
    print('[API] Response: ${r.statusCode}');
    if (r.statusCode != 200) {
      print('[API] Error DELETE id=$id: ${r.statusCode} ${r.body}');
      throw Exception('${r.statusCode}');
    }
  }

  Future<List<R>> getAll() async {
    print('[API] GET ${C.http}${C.allPath}');
    final r = await _call(() => http.get(_url(C.allPath)));
    print('[API] Response: ${r.statusCode}');
    if (r.statusCode != 200) {
      print('[API] Error GET all: ${r.statusCode} ${r.body}');
      throw Exception('${r.statusCode}');
    }
    return (jsonDecode(r.body) as List).map((e) => R.fromJson(e)).toList();
  }
}

//  GATE
class Gate {
  bool listDone = false, allDone = false;
  final Set<int> detailsDone = {};
  void reset() {
    listDone = false;
    allDone = false;
    detailsDone.clear();
    print('[GATE] reset');
  }
}

//  REPOSITORY
class Repo extends ChangeNotifier {
  final DB db;
  final API api;
  final Gate gate;
  Repo(this.db, this.api, this.gate);

  bool offline = false;
  List<R> list = [], all = [];
  final Map<int, R> details = {};

  void setOfflineFromConnectivity(bool connected) {
    if (offline != !connected) {
      offline = !connected;
      print(
        connected ? '[CONNECTIVITY] back online' : '[CONNECTIVITY] offline',
      );
      notifyListeners();
    }
  }

  // A) Load list - GET only once unless retry after offline
  Future<void> loadList({bool force = false}) async {
    if (list.isNotEmpty && !force) return;
    list = await db.getAll();
    notifyListeners();
    if (!gate.listDone) {
      try {
        final r = await api.getList();
        await db.saveAll(r);
        list = r;
        gate.listDone = true;
        offline = false;
      } on Offline catch (e) {
        print('[OFFLINE] loadList: $e');
        offline = true;
      }
      notifyListeners();
    }
  }

  // B) Load details - GET by id only once per id unless retry after offline
  Future<void> loadDetails(int id, {bool force = false}) async {
    final local = await db.getOne(id);
    if (local != null) {
      details[id] = local;
      notifyListeners();
    }
    if (!gate.detailsDone.contains(id) || force) {
      try {
        final r = await api.getOne(id);
        await db.saveOne(r);
        details[id] = r;
        gate.detailsDone.add(id);
        offline = false;
      } on Offline catch (e) {
        print('[OFFLINE] loadDetails id=$id: $e');
        offline = true;
      }
      notifyListeners();
    }
  }

  // C) Add - online only
  Future<void> add(
    String date,
    double amount,
    String type,
    String cat,
    String desc,
  ) async {
    final r = await api.create(date, amount, type, cat, desc);
    await db.saveOne(r);
    list.insert(0, r);
    if (all.isNotEmpty) all.insert(0, r);
    offline = false;
    notifyListeners();
  }

  // D) Delete - online only
  Future<void> del(int id) async {
    await api.delete(id);
    await db.delete(id);
    list.removeWhere((r) => r.id == id);
    all.removeWhere((r) => r.id == id);
    details.remove(id);
    offline = false;
    notifyListeners();
  }

  // Reports & Insights - GET /all once
  Future<void> loadAll({bool force = false}) async {
    if (all.isNotEmpty && !force) return;
    all = await db.getAll();
    notifyListeners();
    if (!gate.allDone) {
      try {
        final r = await api.getAll();
        await db.saveAll(r);
        all = r;
        gate.allDone = true;
        offline = false;
      } on Offline catch (e) {
        print('[OFFLINE] loadAll: $e');
        offline = true;
      }
      notifyListeners();
    }
  }

  // RETRY - resets gates then fetches
  Future<void> retryList() async {
    gate.reset();
    await loadList(force: true);
  }

  Future<void> retryDetails(int id) async {
    gate.reset();
    await loadDetails(id, force: true);
  }

  Future<void> retryAll() async {
    gate.reset();
    await loadAll(force: true);
  }

  // Reports: monthly totals descending
  List<MapEntry<String, double>> monthly() {
    final m = <String, double>{};
    for (var r in all) {
      final k = r.date.length >= 7 ? r.date.substring(0, 7) : '?';
      m[k] = (m[k] ?? 0) + r.amount;
    }
    return m.entries.toList()..sort((a, b) => b.value.compareTo(a.value));
  }

  // Insights: top 3 categories descending
  List<MapEntry<String, double>> top3() {
    final m = <String, double>{};
    for (var r in all) {
      final k = r.category.isEmpty ? '?' : r.category;
      m[k] = (m[k] ?? 0) + r.amount;
    }
    return (m.entries.toList()..sort((a, b) => b.value.compareTo(a.value)))
        .take(3)
        .toList();
  }

  // WebSocket push
  Future<void> onWs(R r) async {
    await db.saveOne(r);
    list.insert(0, r);
    if (all.isNotEmpty) all.insert(0, r);
    notifyListeners();
  }
}

//  WEBSOCKET
class WS {
  final Repo repo;
  final GlobalKey<ScaffoldMessengerState> msgKey;
  WebSocketChannel? _ch;
  StreamSubscription? _sub;
  WS(this.repo, this.msgKey);

  void connect() {
    try {
      _ch = WebSocketChannel.connect(Uri.parse(C.ws));
      print('[WS] connected');
      _sub = _ch!.stream.listen((msg) async {
        try {
          final r = R.fromJson(jsonDecode(msg));
          await repo.onWs(r);
          msgKey.currentState?.showSnackBar(
            SnackBar(content: Text('New ${C.single}: #${r.id}')),
          );
        } catch (e) {
          print('[WS] error: $e');
        }
      });
    } catch (e) {
      print('[WS] failed: $e');
    }
  }

  void close() {
    _sub?.cancel();
    _ch?.sink.close();
  }
}
