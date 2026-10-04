import 'dart:developer' as developer;
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class DBHelper {
  static const _dbName = 'recipes.db';
  static const _table = 'recipes';
  static Database? _db;

  static Future<Database> get db async => _db ??= await _init();

  static Future<Database> _init() async {
    final path = join(await getDatabasesPath(), _dbName);
    return openDatabase(
      path,
      version: 3,
      onCreate: (db, _) async {
        await _createTable(db);
      },
      onUpgrade: (db, oldVersion, newVersion) async {
        if (oldVersion < 3) {
          await db.execute('DROP TABLE IF EXISTS $_table');
          await _createTable(db);
        }
      },
    );
  }

  static Future<void> _createTable(Database db) async {
    await db.execute(
      'CREATE TABLE $_table('
      'id TEXT PRIMARY KEY, '
      'title TEXT, '
      'ingredients TEXT, '
      'steps TEXT, '
      'preparationTime INTEGER, '
      'dateCreated INTEGER, '
      'synced INTEGER DEFAULT 0, '
      'markedForDelete INTEGER DEFAULT 0'
      ')',
    );
    developer.log('Database table created');
  }

  static Future<void> insert(Map<String, dynamic> data) async {
    await (await db).insert(_table, data);
    developer.log('Inserted recipe with ID ${data['id']}');
  }

  static Future<List<Map<String, dynamic>>> getAll() async {
    final result = await (await db).query(_table, orderBy: 'id DESC');
    developer.log('Retrieved ${result.length} recipes from local DB');
    return result;
  }

  static Future<List<Map<String, dynamic>>> getUnsynced() async {
    final result = await (await db).query(
      _table,
      where: 'synced = 0 AND markedForDelete = 0',
    );
    developer.log('Found ${result.length} unsynced recipes');
    return result;
  }

  static Future<List<Map<String, dynamic>>> getMarkedForDelete() async {
    final result = await (await db).query(_table, where: 'markedForDelete = 1');
    developer.log('Found ${result.length} recipes marked for deletion');
    return result;
  }

  static Future<int> update(Map<String, dynamic> data) async {
    final result = await (await db).update(
      _table,
      data,
      where: 'id = ?',
      whereArgs: [data['id']],
    );
    developer.log('Updated recipe ID ${data['id']}');
    return result;
  }

  static Future<int> delete(String id) async {
    final result = await (await db).delete(
      _table,
      where: 'id = ?',
      whereArgs: [id],
    );
    developer.log('Deleted recipe ID $id');
    return result;
  }

  static Future<int> markForDelete(String id) async {
    developer.log('Marking recipe $id for deletion');
    return update({'id': id, 'markedForDelete': 1});
  }

  static Future<void> clear() async {
    await (await db).delete(_table);
    developer.log('Cleared all recipes from local DB');
  }
}
