// =================================================================
// APP CONFIG - CHANGE THESE VALUES FOR YOUR EXAM
// =================================================================
// For Android Emulator: host = "10.0.2.2"
// For real phone on same WiFi: host = your PC's IP (e.g., "192.168.1.10")

class AppConfig {
  static const String host = "192.168.199.193";

  // PORT: Restaurant Order App: 2625
  static const int port = 2625;

  // LABELS (shown in UI)
  static const String appTitle = "Restaurant Order App";
  static const String singular = "order";
  static const String plural = "orders";

  // ENDPOINTS
  static const String listPath = "/orders"; // GET all (main list)
  static const String detailPath = "/order"; // GET /:id
  static const String createPath = "/order"; // POST
  static const String deletePath = "/order"; // DELETE /:id
  static const String allPath = "/allOrders"; // GET all (reports/insights)

  // Built URLs
  static String get baseHttpUrl => "http://$host:$port";
  static String get wsUrl => "ws://$host:$port";
}
