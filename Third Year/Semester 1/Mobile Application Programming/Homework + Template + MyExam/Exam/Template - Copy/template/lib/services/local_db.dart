import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart' as p;

import '../models/record.dart';

// =================================================================
// LOCAL DATABASE - SQLite for offline storage
// =================================================================

class LocalDb {
  static const _dbName = "exam_app.db";
  static const _table = "records";
  Database? _db;

  Future<void> init() async {
    final dbPath = p.join(await getDatabasesPath(), _dbName);
    _db = await openDatabase(
      dbPath,
      version: 1,
      onCreate: (db, _) async {
        await db.execute("""
          CREATE TABLE $_table(
            id INTEGER PRIMARY KEY,
            date TEXT NOT NULL,
            amount REAL NOT NULL,
            type TEXT NOT NULL,
            category TEXT,
            description TEXT
          )
        """);
      },
    );
    print("[DB] Opened at $dbPath");
  }

  Database get db => _db!;

  // Insert or update multiple records
  Future<void> upsertAll(List<Record> items) async {
    final batch = db.batch();
    for (final r in items) {
      batch.insert(_table, r.toDbMap(),
          conflictAlgorithm: ConflictAlgorithm.replace);
    }
    await batch.commit(noResult: true);
    print("[DB] Upserted ${items.length} records");
  }

  // Insert or update one record
  Future<void> upsertOne(Record r) async {
    await db.insert(_table, r.toDbMap(),
        conflictAlgorithm: ConflictAlgorithm.replace);
    print("[DB] Upserted record id=${r.id}");
  }

  // Get all records
  Future<List<Record>> getAll() async {
    final rows = await db.query(_table, orderBy: "date DESC, id DESC");
    return rows.map(Record.fromDbMap).toList();
  }

  // Get one record by id
  Future<Record?> getById(int id) async {
    final rows =
        await db.query(_table, where: "id = ?", whereArgs: [id], limit: 1);
    if (rows.isEmpty) return null;
    return Record.fromDbMap(rows.first);
  }

  // Delete one record
  Future<void> deleteById(int id) async {
    await db.delete(_table, where: "id = ?", whereArgs: [id]);
    print("[DB] Deleted record id=$id");
  }
}
