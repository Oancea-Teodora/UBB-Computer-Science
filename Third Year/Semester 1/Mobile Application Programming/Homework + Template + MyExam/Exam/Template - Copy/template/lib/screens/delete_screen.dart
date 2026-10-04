import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../config.dart';
import '../models/record.dart';
import '../repositories/records_repository.dart';
import '../services/api_service.dart';
import '../widgets/ui_helpers.dart';

// =================================================================
// D) DELETE SCREEN - DELETE (online only, separate screen)
// =================================================================

class DeleteScreen extends StatefulWidget {
  final int id;
  const DeleteScreen({super.key, required this.id});

  @override
  State<DeleteScreen> createState() => _DeleteScreenState();
}

class _DeleteScreenState extends State<DeleteScreen> {
  bool _busy = false;

  Future<void> _confirmDelete() async {
    setState(() => _busy = true);
    try {
      await context.read<RecordsRepository>().deleteOnlineOnly(widget.id);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("${AppConfig.singular} deleted.")),
        );
        Navigator.pop(context);
      }
    } on OfflineException {
      showError(context, "Online only: turn on internet and try again.");
    } on ApiException catch (e) {
      showError(context, "Server error: ${e.status}");
    } catch (e) {
      showError(context, "Error: $e");
    } finally {
      if (mounted) setState(() => _busy = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<RecordsRepository>();
    // Try to find in list cache, then details cache
    final r = repo.listCache.cast<Record?>().firstWhere(
          (x) => x?.id == widget.id,
          orElse: () => repo.detailsCache[widget.id],
        );

    return Scaffold(
      appBar: AppBar(title: Text("Delete ${AppConfig.singular} #${widget.id}")),
      body: loadingOverlay(
        show: _busy,
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text("Are you sure?",
                      style: Theme.of(context).textTheme.titleLarge),
                  const SizedBox(height: 12),
                  if (r != null) ...[
                    Text("ID: ${r.id}"),
                    Text("Date: ${r.date}"),
                    Text("Amount: ${r.amount.toStringAsFixed(2)}"),
                    Text("Type: ${r.type}"),
                    Text("Category: ${r.category}"),
                  ] else
                    Text("ID: ${widget.id}"),
                  const SizedBox(height: 16),
                  ElevatedButton.icon(
                    onPressed: _confirmDelete,
                    icon: const Icon(Icons.delete),
                    label: const Text("Confirm delete (online only)"),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
