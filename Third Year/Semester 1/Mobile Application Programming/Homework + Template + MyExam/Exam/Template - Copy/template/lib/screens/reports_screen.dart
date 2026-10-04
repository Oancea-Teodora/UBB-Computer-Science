import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../repositories/records_repository.dart';
import '../widgets/ui_helpers.dart';

// =================================================================
// REPORTS SCREEN - Monthly Dining Analysis (descending by total)
// =================================================================

class ReportsScreen extends StatefulWidget {
  const ReportsScreen({super.key});

  @override
  State<ReportsScreen> createState() => _ReportsScreenState();
}

class _ReportsScreenState extends State<ReportsScreen> {
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
    final totals = repo.monthlyTotals();

    return Scaffold(
      appBar: AppBar(title: const Text("Monthly Dining Analysis")),
      body: loadingOverlay(
        show: _busy,
        child: Column(
          children: [
            offlineBanner(show: repo.offline, onRetry: _retry),
            Expanded(
              child: totals.isEmpty
                  ? const Center(child: Text("No data yet."))
                  : ListView.builder(
                      itemCount: totals.length,
                      itemBuilder: (_, i) {
                        final e = totals[i];
                        return ListTile(
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
