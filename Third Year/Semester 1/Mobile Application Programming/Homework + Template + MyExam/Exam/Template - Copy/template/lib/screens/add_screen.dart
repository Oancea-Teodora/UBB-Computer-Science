import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../config.dart';
import '../repositories/records_repository.dart';
import '../services/api_service.dart';
import '../widgets/ui_helpers.dart';

// =================================================================
// C) ADD SCREEN - POST (online only)
// =================================================================

class AddScreen extends StatefulWidget {
  const AddScreen({super.key});

  @override
  State<AddScreen> createState() => _AddScreenState();
}

class _AddScreenState extends State<AddScreen> {
  final _date = TextEditingController(text: "2026-02-01");
  final _amount = TextEditingController(text: "25.50");
  final _type = TextEditingController(text: "delivery");
  final _category = TextEditingController(text: "pizza");
  final _desc = TextEditingController(text: "Pepperoni Large");
  bool _busy = false;

  bool _validDate(String s) => RegExp(r"^\d{4}-\d{2}-\d{2}$").hasMatch(s);

  Future<void> _submit() async {
    final date = _date.text.trim();
    final amountStr = _amount.text.trim();
    final type = _type.text.trim();
    final category = _category.text.trim();
    final desc = _desc.text.trim();

    // Validation
    if (!_validDate(date)) {
      showError(context, "Date must be YYYY-MM-DD");
      return;
    }
    final amount = double.tryParse(amountStr);
    if (amount == null) {
      showError(context, "Amount must be a number");
      return;
    }
    if (type.isEmpty) {
      showError(context, "Type is required");
      return;
    }

    setState(() => _busy = true);
    try {
      await context.read<RecordsRepository>().addOnlineOnly(
            date: date,
            amount: amount,
            type: type,
            category: category.isEmpty ? "general" : category,
            description: desc,
          );
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("${AppConfig.singular} added!")),
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
    return Scaffold(
      appBar: AppBar(title: Text("Add ${AppConfig.singular}")),
      body: loadingOverlay(
        show: _busy,
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            TextField(
                controller: _date,
                decoration:
                    const InputDecoration(labelText: "Date (YYYY-MM-DD)")),
            TextField(
                controller: _amount,
                decoration: const InputDecoration(labelText: "Amount"),
                keyboardType: TextInputType.number),
            TextField(
                controller: _type,
                decoration: const InputDecoration(
                    labelText: "Type",
                    hintText: "dine-in, takeout, delivery")),
            TextField(
                controller: _category,
                decoration: const InputDecoration(
                    labelText: "Category",
                    hintText: "pizza, sushi, burger")),
            TextField(
                controller: _desc,
                decoration: const InputDecoration(labelText: "Description")),
            const SizedBox(height: 16),
            ElevatedButton(onPressed: _submit, child: const Text("Create")),
          ],
        ),
      ),
    );
  }
}
