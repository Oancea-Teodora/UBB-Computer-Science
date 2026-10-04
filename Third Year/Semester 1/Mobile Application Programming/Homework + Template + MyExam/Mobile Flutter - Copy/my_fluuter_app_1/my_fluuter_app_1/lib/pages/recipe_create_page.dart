import 'package:flutter/material.dart';
import '../data/recipe_repo.dart';
import '../models/recipe.dart';

class RecipeCreatePage extends StatefulWidget {
  const RecipeCreatePage({super.key});
  @override
  State<RecipeCreatePage> createState() => _RecipeCreatePageState();
}

class _RecipeCreatePageState extends State<RecipeCreatePage> {
  final _form = GlobalKey<FormState>();
  final _title = TextEditingController();
  final _prep = TextEditingController();
  final _ing = TextEditingController();
  final _steps = TextEditingController();
  bool _saving = false;

  void _showError(String msg) {
    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text(msg), backgroundColor: Colors.red));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add Recipe')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _form,
          child: ListView(
            children: [
              TextFormField(
                controller: _title,
                decoration: const InputDecoration(
                  labelText: 'Title *',
                  border: OutlineInputBorder(),
                ),
                validator: (v) =>
                    v == null || v.trim().isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _prep,
                decoration: const InputDecoration(
                  labelText: 'Preparation Time (minutes) *',
                  border: OutlineInputBorder(),
                ),
                keyboardType: TextInputType.number,
                validator: (v) {
                  final n = int.tryParse(v ?? '');
                  return (n == null || n <= 0) ? '> 0' : null;
                },
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _ing,
                decoration: const InputDecoration(
                  labelText: 'Ingredients *',
                  border: OutlineInputBorder(),
                ),
                minLines: 3,
                maxLines: 5,
                validator: (v) =>
                    v == null || v.trim().isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _steps,
                decoration: const InputDecoration(
                  labelText: 'Steps *',
                  border: OutlineInputBorder(),
                ),
                minLines: 4,
                maxLines: 8,
                validator: (v) =>
                    v == null || v.trim().isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: _saving
                    ? null
                    : () async {
                        if (!_form.currentState!.validate()) return;
                        setState(() => _saving = true);
                        final r = Recipe(
                          id: 'temp',
                          title: _title.text.trim(),
                          ingredients: _ing.text.trim(),
                          steps: _steps.text.trim(),
                          preparationTime: int.parse(_prep.text.trim()),
                          dateCreated: DateTime.now(),
                        );
                        final created = await RecipeRepo.add(r);
                        if (!mounted) return;
                        if (created != null) {
                          Navigator.pop(context, created);
                        } else {
                          _showError('Failed to save recipe');
                          setState(() => _saving = false);
                        }
                      },
                child: _saving ? const Text('Saving...') : const Text('Save'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _title.dispose();
    _prep.dispose();
    _ing.dispose();
    _steps.dispose();
    super.dispose();
  }
}
