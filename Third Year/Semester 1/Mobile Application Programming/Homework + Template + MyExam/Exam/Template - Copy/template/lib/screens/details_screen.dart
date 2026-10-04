import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../config.dart';
import '../repositories/records_repository.dart';
import '../widgets/ui_helpers.dart';

// =================================================================
// B) DETAILS SCREEN - GET /singular/:id (once per id) + offline/retry
// =================================================================

class DetailsScreen extends StatefulWidget {
  final int id;
  const DetailsScreen({super.key, required this.id});

  @override
  State<DetailsScreen> createState() => _DetailsScreenState();
}

class _DetailsScreenState extends State<DetailsScreen> {
  bool _busy = false;

  @override
  void initState() {
    super.initState();
    Future.microtask(() async {
      setState(() => _busy = true);
      await context.read<RecordsRepository>().loadDetailsIfNeeded(widget.id);
      setState(() => _busy = false);
    });
  }

  Future<void> _retry() async {
    setState(() => _busy = true);
    await context
        .read<RecordsRepository>()
        .forceReloadDetailsAfterRetry(widget.id);
    setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<RecordsRepository>();
    final r = repo.detailsCache[widget.id];

    return Scaffold(
      appBar: AppBar(title: Text("${AppConfig.singular} #${widget.id}")),
      body: loadingOverlay(
        show: _busy,
        child: Column(
          children: [
            offlineBanner(show: repo.offline, onRetry: _retry),
            Expanded(
              child: r == null
                  ? const Center(child: Text("No local data for this item."))
                  : Padding(
                      padding: const EdgeInsets.all(16),
                      child: Card(
                        child: Padding(
                          padding: const EdgeInsets.all(16),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Text("ID: ${r.id}",
                                  style: const TextStyle(
                                      fontSize: 18,
                                      fontWeight: FontWeight.bold)),
                              const SizedBox(height: 8),
                              Text("Date: ${r.date}"),
                              Text("Amount: ${r.amount.toStringAsFixed(2)}"),
                              Text("Type: ${r.type}"),
                              Text("Category: ${r.category}"),
                              const SizedBox(height: 8),
                              Text("Description: ${r.description}"),
                            ],
                          ),
                        ),
                      ),
                    ),
            ),
          ],
        ),
      ),
    );
  }
}
