import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;

import '../config.dart';
import '../models/record.dart';

// =================================================================
// EXCEPTIONS
// =================================================================

class OfflineException implements Exception {
  final String message;
  OfflineException(this.message);
  @override
  String toString() => "OfflineException($message)";
}

class ApiException implements Exception {
  final int status;
  final String body;
  ApiException(this.status, this.body);
  @override
  String toString() => "ApiException(status=$status, body=$body)";
}

// =================================================================
// API SERVICE - HTTP calls to server
// =================================================================

class ApiService {
  final http.Client _client = http.Client();

  Uri _uri(String path) => Uri.parse("${AppConfig.baseHttpUrl}$path");

  // Wrapper to catch network errors
  Future<http.Response> _safe(Future<http.Response> Function() call) async {
    try {
      return await call().timeout(const Duration(seconds: 8));
    } on SocketException catch (e) {
      throw OfflineException(e.message);
    } on TimeoutException {
      throw OfflineException("Request timed out");
    }
  }

  // GET list of all records
  Future<List<Record>> getList() async {
    print("[API] GET ${AppConfig.listPath}");
    final res = await _safe(() => _client.get(_uri(AppConfig.listPath)));
    if (res.statusCode != 200) throw ApiException(res.statusCode, res.body);

    final raw = jsonDecode(res.body) as List<dynamic>;
    return raw.map((e) => Record.fromJson(e as Map<String, dynamic>)).toList();
  }

  // GET one record by id
  Future<Record> getById(int id) async {
    final path = "${AppConfig.detailPath}/$id";
    print("[API] GET $path");
    final res = await _safe(() => _client.get(_uri(path)));
    if (res.statusCode != 200) throw ApiException(res.statusCode, res.body);

    final raw = jsonDecode(res.body) as Map<String, dynamic>;
    return Record.fromJson(raw);
  }

  // POST create new record
  Future<Record> create({
    required String date,
    required double amount,
    required String type,
    required String category,
    required String description,
  }) async {
    print("[API] POST ${AppConfig.createPath}");
    final payload = jsonEncode({
      "date": date,
      "amount": amount,
      "type": type,
      "category": category,
      "description": description,
    });

    final res = await _safe(() => _client.post(
          _uri(AppConfig.createPath),
          headers: {"Content-Type": "application/json"},
          body: payload,
        ));

    if (res.statusCode != 201) throw ApiException(res.statusCode, res.body);

    final raw = jsonDecode(res.body) as Map<String, dynamic>;
    return Record.fromJson(raw);
  }

  // DELETE one record
  Future<Record> delete(int id) async {
    final path = "${AppConfig.deletePath}/$id";
    print("[API] DELETE $path");
    final res = await _safe(() => _client.delete(_uri(path)));
    if (res.statusCode != 200) throw ApiException(res.statusCode, res.body);

    final raw = jsonDecode(res.body) as Map<String, dynamic>;
    return Record.fromJson(raw);
  }

  // GET all records (for reports/insights)
  Future<List<Record>> getAll() async {
    print("[API] GET ${AppConfig.allPath}");
    final res = await _safe(() => _client.get(_uri(AppConfig.allPath)));
    if (res.statusCode != 200) throw ApiException(res.statusCode, res.body);

    final raw = jsonDecode(res.body) as List<dynamic>;
    return raw.map((e) => Record.fromJson(e as Map<String, dynamic>)).toList();
  }
}
