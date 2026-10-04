import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import '../config.dart';
import '../models/record.dart';
import '../repositories/records_repository.dart';

// =================================================================
// WEBSOCKET SERVICE - Listens for new records from server
// =================================================================

class WsService {
  final RecordsRepository repo;
  final GlobalKey<ScaffoldMessengerState> messengerKey;
  WebSocketChannel? _channel;
  StreamSubscription? _sub;

  WsService({required this.repo, required this.messengerKey});

  void connect() {
    try {
      _channel = WebSocketChannel.connect(Uri.parse(AppConfig.wsUrl));
      print("[WS] Connected to ${AppConfig.wsUrl}");

      _sub = _channel!.stream.listen(
        (msg) async {
          try {
            final raw = jsonDecode(msg as String) as Map<String, dynamic>;
            final r = Record.fromJson(raw);
            await repo.onWsRecord(r);

            // Show SnackBar notification
            messengerKey.currentState?.showSnackBar(
              SnackBar(
                content: Text(
                    "New ${AppConfig.singular}: #${r.id} (${r.amount.toStringAsFixed(2)})"),
              ),
            );
          } catch (e) {
            print("[WS] Parse error: $e");
          }
        },
        onError: (e) => print("[WS] Error: $e"),
        onDone: () => print("[WS] Closed"),
      );
    } catch (e) {
      print("[WS] Connection failed: $e");
    }
  }

  void dispose() {
    _sub?.cancel();
    _channel?.sink.close();
  }
}
