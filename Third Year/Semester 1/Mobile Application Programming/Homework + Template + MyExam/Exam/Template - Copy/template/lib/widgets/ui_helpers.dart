import 'package:flutter/material.dart';

// =================================================================
// UI HELPERS
// =================================================================

// Show error SnackBar
void showError(BuildContext context, String msg) {
  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(msg)));
}

// Offline banner with Retry button
Widget offlineBanner({required bool show, required VoidCallback onRetry}) {
  if (!show) return const SizedBox.shrink();
  return MaterialBanner(
    content: const Text("Offline mode: showing saved data."),
    actions: [
      TextButton(onPressed: onRetry, child: const Text("Retry")),
    ],
  );
}

// Loading overlay (progress indicator)
Widget loadingOverlay({required bool show, required Widget child}) {
  if (!show) return child;
  return Stack(
    children: [
      child,
      Positioned.fill(
        child: Container(
          color: Colors.black26,
          alignment: Alignment.center,
          child: const CircularProgressIndicator(),
        ),
      ),
    ],
  );
}
