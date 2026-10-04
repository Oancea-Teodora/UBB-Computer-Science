import 'package:flutter/material.dart';
import '../data/recipe_repo.dart';
import '../models/recipe.dart';
import '../utils/format.dart';
import 'recipe_edit_page.dart';

class RecipeReadPage extends StatelessWidget {
  final String recipeId;
  const RecipeReadPage({super.key, required this.recipeId});

  void _showError(BuildContext context, String msg) {
    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text(msg), backgroundColor: Colors.red));
  }

  @override
  Widget build(BuildContext context) {
    final recipe = RecipeRepo.recipes.value[recipeId];

    if (recipe == null || recipe.title.isEmpty) {
      return const Scaffold(body: Center(child: Text('Recipe not found')));
    }

    return Scaffold(
      appBar: AppBar(title: const Text('Recipe')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ListView(
          children: [
            Text(
              recipe.title,
              style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 4),
            Text('Created: ${fmtDate(recipe.dateCreated)}'),
            const SizedBox(height: 12),
            Text(
              'Preparation Time: ${recipe.preparationTime} minutes',
              style: const TextStyle(fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 12),
            const Text(
              'Ingredients',
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
            Text(recipe.ingredients),
            const SizedBox(height: 12),
            const Text('Steps', style: TextStyle(fontWeight: FontWeight.bold)),
            Text(recipe.steps),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  child: OutlinedButton.icon(
                    icon: const Icon(Icons.edit),
                    label: const Text('Edit'),
                    onPressed: () async {
                      final res = await Navigator.push<Recipe>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => RecipeEditPage(recipeId: recipe.id),
                        ),
                      );
                      if (res != null && context.mounted) {
                        Navigator.pop(context);
                      }
                    },
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: ElevatedButton.icon(
                    icon: const Icon(Icons.delete),
                    label: const Text('Delete'),
                    onPressed: () async {
                      final ok = await showDialog<bool>(
                        context: context,
                        builder: (_) => AlertDialog(
                          title: const Text('Confirm Delete'),
                          content: const Text(
                            'Do you want to delete this recipe?',
                          ),
                          actions: [
                            TextButton(
                              onPressed: () => Navigator.pop(context, false),
                              child: const Text('Cancel'),
                            ),
                            TextButton(
                              onPressed: () => Navigator.pop(context, true),
                              child: const Text('Delete'),
                            ),
                          ],
                        ),
                      );
                      if (ok == true) {
                        final success = await RecipeRepo.delete(recipe.id);
                        if (success && context.mounted) {
                          Navigator.pop(context);
                        } else if (!success && context.mounted) {
                          _showError(
                            context,
                            RecipeRepo.errorNotifier.value ?? 'Delete failed',
                          );
                        }
                      }
                    },
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
