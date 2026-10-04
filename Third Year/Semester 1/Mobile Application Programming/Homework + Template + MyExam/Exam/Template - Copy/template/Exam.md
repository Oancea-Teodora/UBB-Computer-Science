# Exam Template - Quick Reference

## How to Adapt in 20 Seconds (ONLY change `lib/config.dart`)

### Movie Ticket Budget App (port 2623)
```dart
static const int port = 2623;
static const String appTitle = "Movie Ticket Budget App";
static const String singular = "ticket";
static const String plural = "tickets";
static const String listPath = "/tickets";
static const String detailPath = "/ticket";
static const String createPath = "/ticket";
static const String deletePath = "/ticket";
static const String allPath = "/allTickets";
```

### Restaurant Order App (port 2625) - CURRENT DEFAULT
```dart
static const int port = 2625;
static const String appTitle = "Restaurant Order App";
static const String singular = "order";
static const String plural = "orders";
static const String listPath = "/orders";
static const String detailPath = "/order";
static const String createPath = "/order";
static const String deletePath = "/order";
static const String allPath = "/allOrders";
```

### Fee Management App (port 2620)
```dart
static const int port = 2620;
static const String appTitle = "Fee Management App";
static const String singular = "fee";
static const String plural = "fees";
static const String listPath = "/fees";
static const String detailPath = "/fee";
static const String createPath = "/fee";
static const String deletePath = "/fee";
static const String allPath = "/allFees";
```

### Medical Cost App (port 2624)
```dart
static const int port = 2624;
static const String appTitle = "Medical Cost App";
static const String singular = "expense";
static const String plural = "expenses";
static const String listPath = "/expenses";
static const String detailPath = "/expense";
static const String createPath = "/expense";
static const String deletePath = "/expense";
static const String allPath = "/allExpenses";
```

### Car Rental App (port 2622)
```dart
static const int port = 2622;
static const String appTitle = "Car Rental App";
static const String singular = "rental";
static const String plural = "rentals";
static const String listPath = "/rentals";
static const String detailPath = "/rental";
static const String createPath = "/rental";
static const String deletePath = "/rental";
static const String allPath = "/allRentals";
```

---

## Checklist Before Exam

1. **Server**: `cd server && npm install && npm start`
2. **Flutter**: `flutter pub get`
3. **Run on emulator**: Change `host` to `"10.0.2.2"` in `lib/config.dart`
4. **Run on real phone**: Change `host` to your PC's IP (e.g., `"192.168.1.10"`)

---

## What This Template Does (Grade 10 Requirements)

| Requirement | Points | File |
|-------------|--------|------|
| **A) View list** | 1p | `screens/list_screen.dart` |
| **B) View details** | 2p | `screens/details_screen.dart` |
| **C) Add new** | 1p | `screens/add_screen.dart` |
| **D) Delete** | 1p | `screens/delete_screen.dart` |
| **Reports: Monthly Dining Analysis** | 1p | `screens/reports_screen.dart` |
| **Insights: Top Food Categories** | 1p | `screens/insights_screen.dart` |
| **WebSocket notifications** | 1p | `services/ws_service.dart` |
| **Progress indicator** | 0.5p | `widgets/ui_helpers.dart` |
| **Error handling + logging** | 0.5p | All files have `print()` + SnackBars |

**Total: 10 points**

---

## File Structure

```
lib/
├── main.dart                 # Entry point, creates providers
├── config.dart               # ⭐ CHANGE THIS FOR EXAM
│
├── models/
│   └── record.dart           # Data model (6 fields)
│
├── services/
│   ├── local_db.dart         # SQLite storage
│   ├── call_gate.dart        # Blocks repeated GET calls
│   ├── api_service.dart      # HTTP requests
│   └── ws_service.dart       # WebSocket listener
│
├── repositories/
│   └── records_repository.dart  # Main logic (ChangeNotifier)
│
├── widgets/
│   └── ui_helpers.dart       # offlineBanner(), loadingOverlay()
│
└── screens/
    ├── home_screen.dart      # Navigation buttons
    ├── list_screen.dart      # A) View list
    ├── details_screen.dart   # B) View details
    ├── add_screen.dart       # C) Add new
    ├── delete_screen.dart    # D) Delete
    ├── reports_screen.dart   # Monthly totals
    └── insights_screen.dart  # Top 3 categories
```

---

## Teacher "Traps" This Template Avoids

1. **GET list called only once** - `CallGate.listFetched` blocks repeat calls
2. **GET by id called only once per id** - `CallGate.detailsFetched` set tracks IDs
3. **No auto-reconnect** - App stays offline until user presses "Retry" button
4. **After Retry, gates reset** - `resetAfterOfflineRetry()` clears all flags
5. **Add/Delete are online-only** - Shows "Online only" error when offline
6. **Reports & Insights share one fetch** - Both use `loadAllIfNeeded()` with gate

---

## How to Explain the Code (Key Points)

### 1. Why `CallGate`? (in `services/call_gate.dart`)
> "The teacher said we can only call GET once. This class has boolean flags that block repeated API calls. After going offline and pressing Retry, `resetAll()` clears the flags so we can fetch again."

### 2. Why `loadingOverlay()`? (in `widgets/ui_helpers.dart`)
> "The teacher gives 0.5 points for progress indicator. This shows a spinner during any server operation."

### 3. Why `offlineBanner()` with Retry button?
> "The teacher said the app must NOT auto-reconnect. It only fetches when the user presses Retry."

### 4. Why `LocalDb` (SQLite)? (in `services/local_db.dart`)
> "Once data is fetched, it must be available offline. SQLite stores everything locally."

### 5. Why `WsService` with global `messengerKey`? (in `services/ws_service.dart`)
> "WebSocket runs outside the widget tree. The global key lets us show SnackBars from anywhere."

---

## Default Values in AddScreen (Change if needed)

The `add_screen.dart` has these default values for testing:
```dart
final _date = TextEditingController(text: "2026-02-01");
final _amount = TextEditingController(text: "25.50");
final _type = TextEditingController(text: "delivery");
final _category = TextEditingController(text: "pizza");
final _desc = TextEditingController(text: "Pepperoni Large");
```

Change these based on your exam entity:
- **Restaurant Order**: type: "dine-in/takeout/delivery", category: "pizza/sushi/burger"
- **Fee Management**: type: "fine", category: "late_return"
- **Medical Cost**: type: "consultation", category: "general"

---

## Quick Commands

```bash
# Start server
cd server && npm install && npm start

# Run Flutter app
flutter pub get
flutter run

# Run on specific device
flutter devices              # list devices
flutter run -d <device_id>   # run on specific device
```
