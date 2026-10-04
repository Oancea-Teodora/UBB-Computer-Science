# Exam Template - ONLY CHANGE config.dart!

## Quick Config Examples

### Fee Management (port 2620) - CURRENT
```dart
static const host = "192.168.199.193"; // Your PC IP
static const port = 2620;
static const title = "Fee Management App";
static const single = "fee";
static const plural = "fees";
static const listPath = "/fees";
static const itemPath = "/fee";
static const allPath = "/allFees";
```

### Movie Tickets (port 2623)
```dart
static const port = 2623;
static const title = "Movie Ticket App";
static const single = "ticket";
static const plural = "tickets";
static const listPath = "/tickets";
static const itemPath = "/ticket";
static const allPath = "/allTickets";
```

### Medical Costs (port 2624)
```dart
static const port = 2624;
static const title = "Medical Cost App";
static const single = "expense";
static const plural = "expenses";
static const listPath = "/expenses";
static const itemPath = "/expense";
static const allPath = "/allExpenses";
```

### Car Rentals (port 2622)
```dart
static const port = 2622;
static const title = "Car Rental App";
static const single = "rental";
static const plural = "rentals";
static const listPath = "/rentals";
static const itemPath = "/rental";
static const allPath = "/allRentals";
```

---

## Files (only 4!)

| File | Purpose |
|------|---------|
| `config.dart` | **CHANGE THIS** - host, port, endpoints |
| `data.dart` | Model, DB, API, Gate, Repo, WebSocket |
| `screens.dart` | All 6 screens |
| `main.dart` | Entry point |

---

## All 10 Points Covered

| Requirement | Points | How |
|-------------|--------|-----|
| A) View list | 1p | ListScreen + GET once + offline + retry |
| B) View details | 2p | DetailsScreen + GET by id once + offline |
| C) Add | 1p | AddScreen + POST + online only |
| D) Delete | 1p | DeleteScreen + DELETE + online only |
| Reports | 1p | ReportsScreen + monthly totals desc |
| Insights | 1p | InsightsScreen + top 3 categories |
| WebSocket | 1p | WS class + SnackBar |
| Progress | 0.5p | loading() helper |
| Errors | 0.5p | err() + print() everywhere |

---

## Teacher Traps AVOIDED

1. **GET /list once** - `gate.listDone` blocks
2. **GET /id once per id** - `gate.detailsDone` set
3. **No auto-reconnect** - only `Retry` button fetches
4. **Gates reset on Retry** - `gate.reset()` called
5. **Online only for Add/Delete** - catches `Offline` exception
