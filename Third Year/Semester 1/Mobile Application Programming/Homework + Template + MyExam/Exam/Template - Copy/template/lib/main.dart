import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'config.dart';
import 'services/local_db.dart';
import 'services/api_service.dart';
import 'services/call_gate.dart';
import 'services/ws_service.dart';
import 'repositories/records_repository.dart';
import 'screens/home_screen.dart';

// =================================================================
// MAIN ENTRY POINT
// =================================================================

// Global key for showing SnackBars from WebSocket (outside widget tree)
final GlobalKey<ScaffoldMessengerState> messengerKey =
    GlobalKey<ScaffoldMessengerState>();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Initialize local database
  final db = LocalDb();
  await db.init();

  // Create API service, gate (to block repeated calls), and repository
  final api = ApiService();
  final gate = CallGate();
  final repo = RecordsRepository(db: db, api: api, gate: gate);

  runApp(
    ChangeNotifierProvider.value(
      value: repo,
      child: const AppRoot(),
    ),
  );
}

// =================================================================
// APP ROOT - Starts WebSocket connection
// =================================================================

class AppRoot extends StatefulWidget {
  const AppRoot({super.key});

  @override
  State<AppRoot> createState() => _AppRootState();
}

class _AppRootState extends State<AppRoot> {
  WsService? _ws;

  @override
  void initState() {
    super.initState();
    // Start WebSocket connection
    final repo = context.read<RecordsRepository>();
    _ws = WsService(repo: repo, messengerKey: messengerKey);
    _ws!.connect();
  }

  @override
  void dispose() {
    _ws?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: AppConfig.appTitle,
      scaffoldMessengerKey: messengerKey,
      theme: ThemeData(useMaterial3: true),
      home: const HomeScreen(),
    );
  }
}
