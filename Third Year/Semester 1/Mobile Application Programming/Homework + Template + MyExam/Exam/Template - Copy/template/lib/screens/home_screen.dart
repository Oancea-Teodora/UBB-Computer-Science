import 'package:flutter/material.dart';

import '../config.dart';
import 'list_screen.dart';
import 'reports_screen.dart';
import 'insights_screen.dart';

// =================================================================
// HOME SCREEN - Navigation to all sections
// =================================================================

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(AppConfig.appTitle)),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            ElevatedButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const ListScreen()),
              ),
              child: Text("Main: View ${AppConfig.plural}"),
            ),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const ReportsScreen()),
              ),
              child: const Text("Reports: Monthly Dining Analysis"),
            ),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const InsightsScreen()),
              ),
              child: const Text("Insights: Top Food Categories"),
            ),
          ],
        ),
      ),
    );
  }
}
