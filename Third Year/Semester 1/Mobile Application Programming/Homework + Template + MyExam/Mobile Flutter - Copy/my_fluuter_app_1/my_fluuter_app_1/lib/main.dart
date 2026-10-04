import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import 'data/recipe_repo.dart';
import 'pages/recipe_list_page.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  try {
    await RecipeRepo.init();
  } catch (e) {
    developer.log('Init error: $e');
  }
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'CookBook',
      theme: ThemeData(useMaterial3: true, colorSchemeSeed: Colors.teal),
      home: const MyAppHome(),
    );
  }
}

class MyAppHome extends StatefulWidget {
  const MyAppHome({super.key});

  @override
  State<MyAppHome> createState() => _MyAppHomeState();
}

class _MyAppHomeState extends State<MyAppHome> {
  @override
  Widget build(BuildContext context) {
    return const RecipeListPage();
  }

  @override
  void dispose() {
    RecipeRepo.dispose();
    super.dispose();
  }
}
