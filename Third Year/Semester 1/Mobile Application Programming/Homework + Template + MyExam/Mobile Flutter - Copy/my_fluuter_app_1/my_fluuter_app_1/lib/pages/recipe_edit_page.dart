import 'package:flutter/material.dart';
import '../data/recipe_repo.dart';
import '../models/recipe.dart';
import '../utils/format.dart';

class RecipeEditPage extends StatefulWidget {
  final String recipeId;
  const RecipeEditPage({super.key, required this.recipeId});
  @override
  State<RecipeEditPage> createState() => _RecipeEditPageState();
}

class _RecipeEditPageState extends State<RecipeEditPage> {
  final _form = GlobalKey<FormState>();
  late Recipe recipe;
  bool _saving = false;

  final _title = TextEditingController();
  final _prep = TextEditingController();
  final _ing = TextEditingController();
  final _steps = TextEditingController();

  @override
  void initState() {
    super.initState();
    recipe = RecipeRepo.recipes.value[widget.recipeId]!;
    _title.text = recipe.title;
    _prep.text = recipe.preparationTime.toString();
    _ing.text = recipe.ingredients;
    _steps.text = recipe.steps;
  }

  void _showError(String msg) {
    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text(msg), backgroundColor: Colors.red));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Edit Recipe')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _form,
          child: ListView(
            children: [
              Text('Created: ${fmtDate(recipe.dateCreated)}'),
              const SizedBox(height: 8),
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
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: _saving
                          ? null
                          : () async {
                              if (!_form.currentState!.validate()) return;
                              setState(() => _saving = true);
                              final updated = Recipe(
                                id: recipe.id,
                                title: _title.text.trim(),
                                ingredients: _ing.text.trim(),
                                steps: _steps.text.trim(),
                                preparationTime: int.parse(_prep.text.trim()),
                                dateCreated: recipe.dateCreated,
                              );
                              final success = await RecipeRepo.update(updated);
                              if (!mounted) return;
                              if (success) {
                                Navigator.pop(context, updated);
                              } else {
                                _showError(
                                  RecipeRepo.errorNotifier.value ??
                                      'Update failed',
                                );
                                setState(() => _saving = false);
                              }
                            },
                      child: _saving
                          ? const Text('Saving...')
                          : const Text('Save'),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: OutlinedButton(
                      onPressed: _saving
                          ? null
                          : () async {
                              final ok = await showDialog<bool>(
                                context: context,
                                builder: (_) => AlertDialog(
                                  title: const Text('Confirm Delete'),
                                  content: const Text('Do you want to delete?'),
                                  actions: [
                                    TextButton(
                                      onPressed: () =>
                                          Navigator.pop(context, false),
                                      child: const Text('Cancel'),
                                    ),
                                    TextButton(
                                      onPressed: () =>
                                          Navigator.pop(context, true),
                                      child: const Text('Delete'),
                                    ),
                                  ],
                                ),
                              );
                              if (!mounted) return;
                              if (ok == true) {
                                final success = await RecipeRepo.delete(
                                  recipe.id,
                                );
                                if (!mounted) return;
                                if (success) {
                                  Navigator.pop(context);
                                } else {
                                  _showError(
                                    RecipeRepo.errorNotifier.value ??
                                        'Delete failed',
                                  );
                                }
                              }
                            },
                      child: const Text('Delete'),
                    ),
                  ),
                ],
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
