import 'dart:async';
import 'package:flutter/material.dart';
import '../data/recipe_repo.dart';
import '../models/recipe.dart';
import '../utils/format.dart';
import '../utils/stream_extensions.dart';
import 'recipe_create_page.dart';
import 'recipe_read_page.dart';

class RecipeListPage extends StatefulWidget {
  const RecipeListPage({super.key});
  @override
  State<RecipeListPage> createState() => _RecipeListPageState();
}

class _RecipeListPageState extends State<RecipeListPage> {
  final _textController = TextEditingController();
  final _searchController = StreamController<String>();
  final _filteredRecipesController = StreamController<List<Recipe>>.broadcast();
  StreamSubscription? _searchSubscription;
  StreamSubscription? _errorSubscription;
  String _currentQuery = '';

  @override
  void initState() {
    super.initState();

    _searchSubscription = _searchController.stream
        .debounceTime(const Duration(milliseconds: 300))
        .listen((query) {
          _currentQuery = query;
          _applyFilter();
        });

    RecipeRepo.recipes.addListener(_applyFilter);

    _errorSubscription =
        Stream.periodic(
          const Duration(milliseconds: 100),
          (_) => RecipeRepo.errorNotifier.value,
        ).distinct().listen((msg) {
          if (msg != null && mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text(msg), backgroundColor: Colors.red),
            );
          }
        });

    _searchController.add('');

    print(
      'initState: isOnlineNotifier value = ${RecipeRepo.isOnlineNotifier.value}',
    );
  }

  void _applyFilter() {
    final allRecipes = RecipeRepo.recipes.value.values.toList();
    if (_currentQuery.isEmpty) {
      _filteredRecipesController.add(allRecipes);
    } else {
      final q = _currentQuery.trim().toUpperCase();
      final filtered = allRecipes
          .where(
            (recipe) =>
                recipe.title.toUpperCase().contains(q) ||
                recipe.ingredients.toUpperCase().contains(q) ||
                recipe.steps.toUpperCase().contains(q),
          )
          .toList();
      _filteredRecipesController.add(filtered);
    }
  }

  Future<void> _openCreate() async {
    await Navigator.push<Recipe>(
      context,
      MaterialPageRoute(builder: (_) => const RecipeCreatePage()),
    );
  }

  Future<void> _openRead(Recipe r) async {
    await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => RecipeReadPage(recipeId: r.id)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('My Recipes'),
        actions: [
          ValueListenableBuilder(
            valueListenable: RecipeRepo.isOnlineNotifier,
            builder: (_, isOnline, __) {
              print('UI Builder called with isOnline: $isOnline');
              return Padding(
                padding: const EdgeInsets.all(16),
                child: Center(
                  child: Text(
                    isOnline ? '✓ Online' : '⚠ Offline',
                    style: TextStyle(
                      color: isOnline ? Colors.green : Colors.orange,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              );
            },
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _openCreate,
        child: const Icon(Icons.add),
      ),
      body: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ValueListenableBuilder(
              valueListenable: RecipeRepo.recipes,
              builder: (_, recipes, __) => Text(
                '${recipes.length} recipes',
                style: const TextStyle(fontWeight: FontWeight.w600),
              ),
            ),
            const SizedBox(height: 8),
            TextField(
              key: const Key('search_field'),
              controller: _textController,
              onChanged: (query) {
                if (!_searchController.isClosed) {
                  _searchController.add(query);
                }
              },
              decoration: const InputDecoration(
                labelText: 'Search recipes',
                hintText: 'Title, ingredients, or steps...',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.search),
              ),
            ),
            const SizedBox(height: 12),
            Expanded(
              child: StreamBuilder<List<Recipe>>(
                stream: _filteredRecipesController.stream,
                builder: (context, snapshot) {
                  if (!snapshot.hasData) {
                    return const Center(child: CircularProgressIndicator());
                  }
                  final recipes = snapshot.data!;
                  return recipes.isEmpty
                      ? const Center(child: Text('No recipes'))
                      : ListView.builder(
                          itemCount: recipes.length,
                          itemBuilder: (_, i) {
                            final e = recipes[i];
                            return ListTile(
                              title: Text(
                                e.title,
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              subtitle: Text(
                                'Prep: ${e.preparationTime} min ;   ${fmtDate(e.dateCreated)}',
                              ),
                              trailing: e.synced
                                  ? null
                                  : const Tooltip(
                                      message: 'Syncing...',
                                      child: Icon(Icons.cloud_upload, size: 16),
                                    ),
                              onTap: () => _openRead(e),
                            );
                          },
                        );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    RecipeRepo.recipes.removeListener(_applyFilter);
    _searchSubscription?.cancel();
    _errorSubscription?.cancel();
    _textController.dispose();
    _searchController.close();
    _filteredRecipesController.close();
    super.dispose();
  }
}
