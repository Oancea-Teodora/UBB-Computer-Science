import 'dart:async';
import 'dart:developer' as developer;
import 'dart:math';
import 'package:flutter/foundation.dart';
import '../models/recipe.dart';
import '../services/network_service.dart';
import 'db_helper.dart';

class RecipeRepo {
  static final recipes = ValueNotifier<Map<String, Recipe>>({});
  static final errorNotifier = ValueNotifier<String?>(null);
  static final isOnlineNotifier = ValueNotifier<bool>(false);
  static final isSyncingNotifier = ValueNotifier<bool>(false);

  static late StreamSubscription<bool> _connectionSub;
  static late StreamSubscription<Map<String, dynamic>> _wsSub;
  static Timer? _syncTimer;

  static Future<void> init() async {
    try {
      developer.log('Initializing RecipeRepo');

      await _load();

      _setupConnectionListener();

      _setupWebSocketListener();

      await _connectServer();

      _startSyncTimer();

      if (recipes.value.isEmpty) await _seedIfEmpty();

      developer.log('RecipeRepo initialized');
    } catch (e) {
      _setError('Init error: ${e.toString()}');
    }
  }

  static Future<void> _load() async {
    try {
      final data = await DBHelper.getAll();
      final recipeMap = <String, Recipe>{};
      for (var m in data) {
        final recipe = Recipe.fromMap(m);
        recipeMap[recipe.id] = recipe;
      }
      recipes.value = recipeMap;
      developer.log('Loaded ${recipes.value.length} recipes from local DB');
    } catch (e) {
      _setError('Load error: ${e.toString()}');
    }
  }

  static Future<void> _connectServer() async {
    try {
      developer.log('Connecting to server...');
      await NetworkService.connectWebSocket();
      developer.log('Server connection successful');

      try {
        final serverRecipes = await NetworkService.getRecipes();
        developer.log('Fetched ${serverRecipes.length} recipes from server');

        final updatedMap = {...recipes.value};
        for (var serverRecipe in serverRecipes) {
          final serverId = serverRecipe['id'];

          final localRecipe = updatedMap[serverId];

          if (localRecipe != null && !localRecipe.synced) {
            final updated = Recipe.fromMap({
              ...localRecipe.toMap(),
              'synced': 1,
              ...serverRecipe,
            });
            updatedMap[serverId] = updated;
            await DBHelper.update(updated.toMap());
          } else if (localRecipe == null) {
            final newRecipe = Recipe.fromMap({...serverRecipe, 'synced': 1});
            updatedMap[serverId] = newRecipe;
            await DBHelper.insert(newRecipe.toMap());
          }
        }
        recipes.value = updatedMap;
        _clearError();
      } catch (e) {
        developer.log('Initial sync failed (non-critical): $e');
      }
    } catch (e) {
      developer.log('Server connection failed: $e');
    }
  }

  static void _setupWebSocketListener() {
    _wsSub = NetworkService.wsStream.listen((message) {
      final type = message['type'];
      final data = message['data'];

      developer.log('WS event: $type', level: 800);

      switch (type) {
        case 'snapshot':
          _handleSnapshot(data);
          break;
        case 'created':
          _handleCreated(data);
          break;
        case 'updated':
          _handleUpdated(data);
          break;
        case 'deleted':
          _handleDeleted(data);
          break;
      }
    });
  }

  static void _setupConnectionListener() {
    developer.log(
      'Setting up connection listener, current isOnline: ${isOnlineNotifier.value}',
    );
    _connectionSub = NetworkService.connectionStream.listen((connected) {
      developer.log('Connection state changed to: $connected');
      isOnlineNotifier.value = connected;
      developer.log('isOnlineNotifier updated to: ${isOnlineNotifier.value}');

      if (connected) {
        _clearError();

        _syncWithServer();
      } else {
        _setError('Offline mode - changes will sync when online');
      }
    });
  }

  static void _startSyncTimer() {
    _syncTimer = Timer.periodic(Duration(seconds: 30), (_) {
      if (isOnlineNotifier.value) {
        _syncWithServer();
      }
    });
  }

  static void _handleSnapshot(dynamic data) {
    developer.log('Handling snapshot with ${(data as List).length} recipes');
  }

  static void _handleCreated(dynamic data) {
    final recipe = Recipe.fromMap(data);
    developer.log('WS: Recipe created on server - ID ${recipe.id}');
    _updateOrAddRecipe(recipe);
  }

  static void _handleUpdated(dynamic data) {
    final recipe = Recipe.fromMap(data);
    developer.log('WS: Recipe updated on server - ID ${recipe.id}');
    _updateOrAddRecipe(recipe);
  }

  static void _handleDeleted(dynamic data) {
    final id = data['id'];
    developer.log('WS: Recipe deleted on server - ID $id');
    final recipeToDelete = recipes.value[id];
    if (recipeToDelete != null) {
      final updatedMap = {...recipes.value};
      updatedMap.remove(id);
      recipes.value = updatedMap;
    }
  }

  static void _updateOrAddRecipe(Recipe wsRecipe) {
    final updatedMap = {...recipes.value};
    final id = wsRecipe.id;

    final existingRecipe = updatedMap[id];

    if (existingRecipe != null) {
      final updated = wsRecipe.copyWith(synced: true);
      updatedMap[id] = updated;
      developer.log('Updated recipe $id with WS data');
      DBHelper.update(updated.toMap());
    } else {
      wsRecipe.synced = true;
      updatedMap[id] = wsRecipe;
      developer.log('Added new recipe $id from WS');
      DBHelper.insert(wsRecipe.toMap());
    }

    recipes.value = updatedMap;
  }

  static Future<Recipe?> add(Recipe r) async {
    try {
      developer.log('Adding recipe: ${r.title}');

      final id =
          '${DateTime.now().millisecondsSinceEpoch}_${Random().nextInt(10000)}';
      final created = Recipe(
        id: id,
        title: r.title,
        ingredients: r.ingredients,
        steps: r.steps,
        preparationTime: r.preparationTime,
        dateCreated: r.dateCreated,
        synced: false,
      );

      await DBHelper.insert(created.toMap());

      final updatedMap = {...recipes.value};
      updatedMap[id] = created;
      recipes.value = updatedMap;

      if (isOnlineNotifier.value) {
        try {
          await NetworkService.createRecipe(
            id: id,
            title: r.title,
            ingredients: r.ingredients,
            steps: r.steps,
            preparationTime: r.preparationTime,
            dateCreated: r.dateCreated.millisecondsSinceEpoch,
          );

          created.synced = true;
          await DBHelper.update(created.toMap());

          final syncedMap = {...recipes.value};
          syncedMap[id] = created;
          recipes.value = syncedMap;

          developer.log('Recipe $id synced to server');
        } catch (e) {
          developer.log('Failed to sync recipe to server: $e');
        }
      } else {
        developer.log('Offline: Recipe saved locally, will sync when online');
      }

      _clearError();
      return created;
    } catch (e) {
      _setError('Add error: ${e.toString()}');
      return null;
    }
  }

  static Future<bool> update(Recipe r) async {
    try {
      developer.log('Updating recipe: ${r.title}');

      await DBHelper.update(r.toMap());
      final updatedMap = {...recipes.value};
      if (updatedMap.containsKey(r.id)) {
        updatedMap[r.id] = r;
        recipes.value = updatedMap;
      }

      if (isOnlineNotifier.value && r.synced) {
        try {
          await NetworkService.updateRecipe(
            id: r.id,
            title: r.title,
            ingredients: r.ingredients,
            steps: r.steps,
            preparationTime: r.preparationTime,
            dateCreated: r.dateCreated.millisecondsSinceEpoch,
          );
          developer.log('Recipe ${r.id} synced to server');
        } catch (e) {
          developer.log('Failed to sync recipe update: $e');
          _setError('Update synced locally, will retry online');
        }
      } else if (!isOnlineNotifier.value) {
        developer.log('Offline: Recipe updated locally, will sync when online');
        _setError('Changes saved locally, will sync when online');
      } else if (isOnlineNotifier.value) {
        _syncWithServer();
      }

      return true;
    } catch (e) {
      _setError('Update error: ${e.toString()}');
      return false;
    }
  }

  static Future<bool> delete(String id) async {
    try {
      developer.log('Deleting recipe: $id');

      final recipe = recipes.value[id];
      if (recipe == null) {
        throw 'Recipe not found';
      }

      await DBHelper.markForDelete(id);
      recipe.markedForDelete = true;

      if (isOnlineNotifier.value && recipe.synced) {
        try {
          await NetworkService.deleteRecipe(id);

          await DBHelper.delete(id);
          final updatedMap = {...recipes.value};
          updatedMap.remove(id);
          recipes.value = updatedMap;
          developer.log('Recipe $id deleted from server and local DB');
        } catch (e) {
          developer.log('Failed to sync deletion: $e');
          _setError('Delete queued, will sync when online');
        }
      } else if (!isOnlineNotifier.value) {
        developer.log(
          'Offline: Recipe marked for deletion, will sync when online',
        );
        _setError('Delete queued, will sync when online');
      } else if (isOnlineNotifier.value) {
        _syncWithServer();
      }

      _clearError();
      return true;
    } catch (e) {
      _setError('Delete error: ${e.toString()}');
      return false;
    }
  }

  static Future<void> _syncWithServer() async {
    if (isSyncingNotifier.value) return;

    try {
      isSyncingNotifier.value = true;
      developer.log('Starting sync with server');

      final unsynced = await DBHelper.getUnsynced();
      for (var recipeMap in unsynced) {
        try {
          final recipe = Recipe.fromMap(recipeMap);
          await NetworkService.createRecipe(
            id: recipe.id,
            title: recipe.title,
            ingredients: recipe.ingredients,
            steps: recipe.steps,
            preparationTime: recipe.preparationTime,
            dateCreated: recipe.dateCreated.millisecondsSinceEpoch,
          );
          recipe.synced = true;
          await DBHelper.update(recipe.toMap());
          developer.log('Synced unsynced recipe ${recipe.id}');
        } catch (e) {
          developer.log('Failed to sync recipe: $e');
        }
      }

      final deleted = await DBHelper.getMarkedForDelete();
      for (var recipeMap in deleted) {
        try {
          final recipe = Recipe.fromMap(recipeMap);
          await NetworkService.deleteRecipe(recipe.id);
          await DBHelper.delete(recipe.id);
          developer.log('Synced deleted recipe ${recipe.id}');
        } catch (e) {
          developer.log('Failed to sync deletion: $e');
        }
      }

      developer.log('Sync completed');
    } catch (e) {
      developer.log('Sync error: $e');
    } finally {
      isSyncingNotifier.value = false;
    }
  }

  static void _setError(String msg) {
    errorNotifier.value = msg;
    developer.log('Error: $msg');
  }

  static void _clearError() {
    errorNotifier.value = null;
  }

  static Future<void> _seedIfEmpty() async {
    if (recipes.value.isNotEmpty) return;
    final today = DateTime.now();

    await add(
      Recipe(
        id: 'seed_1',
        title: 'Tort de ciocolată',
        ingredients: 'Făină, zahăr, lapte, ouă, cacao',
        steps:
            '1. Amestecă ingredientele\n2. Coace la 180°C\n3. Lasă să se răcească',
        preparationTime: 90,
        dateCreated: today,
      ),
    );

    await add(
      Recipe(
        id: 'seed_2',
        title: 'Orez cu lapte',
        ingredients: 'Orez, lapte, zahăr, vanilie',
        steps:
            '1. Fierbe orezul\n2. Adaugă laptele și zahărul\n3. Gătește până se îngroașa',
        preparationTime: 40,
        dateCreated: today,
      ),
    );
  }

  static void dispose() {
    _connectionSub.cancel();
    _wsSub.cancel();
    _syncTimer?.cancel();
    NetworkService.dispose();
  }
}
