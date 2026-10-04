import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../repositories/records_repository.dart';
import '../widgets/ui_helpers.dart';

// =================================================================
// INSIGHTS SCREEN - Top Food Categories (Top 3 by spending)
// =================================================================

class InsightsScreen extends StatefulWidget {
  const InsightsScreen({super.key});

  @override
  State<InsightsScreen> createState() => _InsightsScreenState();
}

class _InsightsScreenState extends State<InsightsScreen> {
  bool _busy = false;

  @override
  void initState() {
    super.initState();
    Future.microtask(() async {
      setState(() => _busy = true);
      await context.read<RecordsRepository>().loadAllIfNeeded();
      setState(() => _busy = false);
    });
  }

  Future<void> _retry() async {
    setState(() => _busy = true);
    await context.read<RecordsRepository>().forceReloadAllAfterRetry();
    setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<RecordsRepository>();
    final top = repo.top3Categories();

    return Scaffold(
      appBar: AppBar(title: const Text("Top Food Categories")),
      body: loadingOverlay(
        show: _busy,
        child: Column(
          children: [
            offlineBanner(show: repo.offline, onRetry: _retry),
            Expanded(
              child: top.isEmpty
                  ? const Center(child: Text("No data yet."))
                  : ListView.builder(
                      itemCount: top.length,
                      itemBuilder: (_, i) {
                        final e = top[i];
                        return ListTile(
                          leading: CircleAvatar(child: Text("#${i + 1}")),
                          title: Text(e.key),
                          trailing: Text(e.value.toStringAsFixed(2)),
                        );
                      },
                    ),
            ),
          ],
        ),
      ),
    );
  }
}
