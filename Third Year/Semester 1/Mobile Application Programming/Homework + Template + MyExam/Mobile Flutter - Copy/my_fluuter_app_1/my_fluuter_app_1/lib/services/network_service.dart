import 'dart:async';
import 'dart:convert';
import 'dart:developer' as developer;
import 'package:http/http.dart' as http;
import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

class NetworkService {
  static const String _baseUrl = 'http://172.30.168.1:8000';
  static const String _wsUrl = 'ws://172.30.168.1:8000/ws';

  static final http.Client _httpClient = http.Client();
  static WebSocketChannel? _wsChannel;

  static final _wsStreamController =
      StreamController<Map<String, dynamic>>.broadcast();
  static final _connectionStateController = StreamController<bool>.broadcast();

  static StreamSubscription<ConnectivityResult>? _connectivitySub;
  static bool _hasNetworkConnection = true;

  static Stream<Map<String, dynamic>> get wsStream =>
      _wsStreamController.stream;
  static Stream<bool> get connectionStream => _connectionStateController.stream;
  static bool get isConnected => _wsChannel != null && _hasNetworkConnection;

  static Future<void> connectWebSocket() async {
    try {
      developer.log('Connecting to WebSocket...');

      _startConnectivityMonitoring();

      _wsChannel = WebSocketChannel.connect(Uri.parse(_wsUrl));

      _wsChannel!.stream.listen(
        (message) {
          try {
            final data = jsonDecode(message);
            developer.log('WS received: ${data['type']}', level: 800);
            _wsStreamController.add(data);
          } catch (e) {
            developer.log('WS parse error: $e');
          }
        },
        onError: (error) {
          developer.log('WS error: $error');
          _connectionStateController.add(false);
          _wsChannel = null;
        },
        onDone: () {
          developer.log('WS disconnected');
          _connectionStateController.add(false);
          _wsChannel = null;
        },
      );

      _connectionStateController.add(true);
      developer.log('WebSocket connected');
    } catch (e) {
      developer.log('WebSocket connection failed: $e');
      _connectionStateController.add(false);
      rethrow;
    }
  }

  static Future<void> _startConnectivityMonitoring() async {
    final initialResult = await Connectivity().checkConnectivity();
    _hasNetworkConnection = initialResult != ConnectivityResult.none;
    developer.log(
      'Initial connectivity: $_hasNetworkConnection (result: $initialResult)',
    );

    _connectivitySub ??= Connectivity().onConnectivityChanged.listen((result) {
      final hasConnection = result != ConnectivityResult.none;

      developer.log('Connectivity changed: $hasConnection (result: $result)');

      if (!hasConnection && _hasNetworkConnection) {
        _hasNetworkConnection = false;
        _connectionStateController.add(false);
        developer.log('Network disconnected - offline mode');
      } else if (hasConnection && !_hasNetworkConnection) {
        _hasNetworkConnection = true;
        developer.log('Network reconnected - attempting to sync');

        if (_wsChannel == null) {
          connectWebSocket().catchError((e) {
            developer.log('Reconnect failed: $e');
          });
        } else {
          _connectionStateController.add(true);
        }
      }
    });
  }

  static Future<void> disconnectWebSocket() async {
    try {
      await _wsChannel?.sink.close();
      _wsChannel = null;
      _connectivitySub?.cancel();
      _connectivitySub = null;
      _connectionStateController.add(false);
      developer.log('WebSocket disconnected');
    } catch (e) {
      developer.log('WebSocket disconnect error: $e');
    }
  }

  static Future<List<Map<String, dynamic>>> getRecipes() async {
    try {
      developer.log('GET /recipes');
      final response = await _httpClient
          .get(
            Uri.parse('$_baseUrl/recipes'),
            headers: {'Content-Type': 'application/json'},
          )
          .timeout(const Duration(seconds: 10));

      if (response.statusCode == 200) {
        final recipes = jsonDecode(response.body) as List;
        developer.log(
          'GET /recipes returned ${recipes.length} recipes',
          level: 800,
        );
        return recipes.cast<Map<String, dynamic>>();
      } else {
        final error = _parseError(response);
        throw error;
      }
    } catch (e) {
      developer.log('GET /recipes failed: $e');
      rethrow;
    }
  }

  static Future<Map<String, dynamic>> createRecipe({
    required String id,
    required String title,
    required String ingredients,
    required String steps,
    required int preparationTime,
    required int dateCreated,
  }) async {
    try {
      developer.log('POST /recipes - title: $title');
      final body = jsonEncode({
        'id': id,
        'title': title,
        'ingredients': ingredients,
        'steps': steps,
        'preparationTime': preparationTime,
        'dateCreated': dateCreated,
      });

      final response = await _httpClient
          .post(
            Uri.parse('$_baseUrl/recipes'),
            headers: {'Content-Type': 'application/json'},
            body: body,
          )
          .timeout(const Duration(seconds: 10));

      if (response.statusCode == 200) {
        final recipe = jsonDecode(response.body) as Map<String, dynamic>;
        developer.log('Created recipe ID ${recipe['id']}', level: 800);
        return recipe;
      } else {
        final error = _parseError(response);
        throw error;
      }
    } catch (e) {
      developer.log('POST /recipes failed: $e');
      rethrow;
    }
  }

  static Future<Map<String, dynamic>> updateRecipe({
    required String id,
    required String title,
    required String ingredients,
    required String steps,
    required int preparationTime,
    required int dateCreated,
  }) async {
    try {
      developer.log('PUT /recipes/$id - title: $title');
      final body = jsonEncode({
        'title': title,
        'ingredients': ingredients,
        'steps': steps,
        'preparationTime': preparationTime,
        'dateCreated': dateCreated,
      });

      final response = await _httpClient
          .put(
            Uri.parse('$_baseUrl/recipes/$id'),
            headers: {'Content-Type': 'application/json'},
            body: body,
          )
          .timeout(const Duration(seconds: 10));

      if (response.statusCode == 200) {
        final recipe = jsonDecode(response.body) as Map<String, dynamic>;
        developer.log('Updated recipe ID $id', level: 800);
        return recipe;
      } else {
        final error = _parseError(response);
        throw error;
      }
    } catch (e) {
      developer.log('PUT /recipes/$id failed: $e');
      rethrow;
    }
  }

  static Future<void> deleteRecipe(String id) async {
    try {
      developer.log('DELETE /recipes/$id');
      final response = await _httpClient
          .delete(
            Uri.parse('$_baseUrl/recipes/$id'),
            headers: {'Content-Type': 'application/json'},
          )
          .timeout(const Duration(seconds: 10));

      if (response.statusCode != 200) {
        final error = _parseError(response);
        throw error;
      }
      developer.log('Deleted recipe ID $id', level: 800);
    } catch (e) {
      developer.log('DELETE /recipes/$id failed: $e');
      rethrow;
    }
  }

  static String _parseError(http.Response response) {
    try {
      final body = jsonDecode(response.body);
      return body['error'] ?? 'Server error';
    } catch (e) {
      return 'Network error: ${response.statusCode}';
    }
  }

  static void dispose() {
    _connectivitySub?.cancel();
    _wsStreamController.close();
    _connectionStateController.close();
    _httpClient.close();
  }
}
