import 'dart:async';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'config.dart';
import 'data.dart';
import 'screens.dart';

final msgKey = GlobalKey<ScaffoldMessengerState>();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final db = DB(); await db.init();
  final repo = Repo(db, API(), Gate());
  runApp(ChangeNotifierProvider.value(value: repo, child: const App()));
}

class App extends StatefulWidget {
  const App({super.key});
  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  WS? _ws;
  StreamSubscription<List<ConnectivityResult>>? _connectivitySub;

  static bool _isConnected(List<ConnectivityResult> result) {
    if (result.isEmpty) return false;
    return result.any((r) => r != ConnectivityResult.none);
  }

  @override
  void initState() {
    super.initState();
    _ws = WS(context.read<Repo>(), msgKey);
    _ws!.connect();
    _connectivitySub = Connectivity().onConnectivityChanged.listen((result) {
      context.read<Repo>().setOfflineFromConnectivity(_isConnected(result));
    });
  }

  @override
  void dispose() {
    _connectivitySub?.cancel();
    _ws?.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) =>
      MaterialApp(scaffoldMessengerKey: msgKey, title: C.title, home: const HomeScreen());
}
