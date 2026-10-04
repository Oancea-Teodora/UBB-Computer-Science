// ========== CHANGE ONLY THIS FILE FOR EXAM ==========
// Emulator: "10.0.2.2" | Real phone WiFi: your PC IP
class C {
  static const host = "192.168.199.193";
  static const port = 2627;
  
  // Change these for your exam entity:
  static const title = "Salary Management App";
  static const single = "payment";
  static const plural = "payments";
  static const listPath = "/payments";
  static const itemPath = "/payment";
  static const allPath = "/allPayments";

  static String get http => "http://$host:$port";
  static String get ws => "ws://$host:$port";
}
