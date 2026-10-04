import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../config.dart';
import '../repositories/records_repository.dart';
import '../widgets/ui_helpers.dart';
import 'details_screen.dart';
import 'delete_screen.dart';
import 'add_screen.dart';

// =================================================================
// A) LIST SCREEN - GET /plural + offline/retry + cache
// =================================================================

class ListScreen extends StatefulWidget {
  const ListScreen({super.key});

  @override
  State<ListScreen> createState() => _ListScreenState();
}

class _ListScreenState extends State<ListScreen> {
  bool _busy = false;

  @override
  void initState() {
    super.initState();
    Future.microtask(() async {
      setState(() => _busy = true);
      await context.read<RecordsRepository>().loadListIfNeeded();
      setState(() => _busy = false);
    });
  }

  Future<void> _retry() async {
    setState(() => _busy = true);
    await context.read<RecordsRepository>().forceReloadListAfterRetry();
    setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<RecordsRepository>();
    final items = repo.listCache;

    return Scaffold(
      appBar: AppBar(title: Text("List of ${AppConfig.plural}")),
      floatingActionButton: FloatingActionButton(
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const AddScreen()),
        ),
        child: const Icon(Icons.add),
      ),
      body: loadingOverlay(
        show: _busy,
        child: Column(
          children: [
            offlineBanner(show: repo.offline, onRetry: _retry),
            Expanded(
              child: items.isEmpty
                  ? const Center(child: Text("No data yet."))
                  : ListView.builder(
                      itemCount: items.length,
                      itemBuilder: (_, i) {
                        final r = items[i];
                        return ListTile(
                          title: Text(
                              "#${r.id} - ${r.category} - ${r.amount.toStringAsFixed(2)}"),
                          subtitle: Text(
                              "${r.date} - ${r.type}\n${r.description}"),
                          isThreeLine: true,
                          onTap: () => Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (_) => DetailsScreen(id: r.id)),
                          ),
                          trailing: IconButton(
                            icon: const Icon(Icons.delete),
                            onPressed: () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (_) => DeleteScreen(id: r.id)),
                            ),
                          ),
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
