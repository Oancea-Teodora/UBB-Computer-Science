// =================================================================
// MODEL - The data structure (same for all exam variants)
// =================================================================

class Record {
  final int id;
  final String date; // "YYYY-MM-DD"
  final double amount;
  final String type;
  final String category;
  final String description;

  Record({
    required this.id,
    required this.date,
    required this.amount,
    required this.type,
    required this.category,
    required this.description,
  });

  // Parse from JSON (server response)
  factory Record.fromJson(Map<String, dynamic> json) {
    return Record(
      id: (json['id'] as num).toInt(),
      date: (json['date'] ?? "").toString(),
      amount: (json['amount'] as num).toDouble(),
      type: (json['type'] ?? "").toString(),
      category: (json['category'] ?? "").toString(),
      description: (json['description'] ?? "").toString(),
    );
  }

  // Convert to Map for SQLite storage
  Map<String, dynamic> toDbMap() => {
        "id": id,
        "date": date,
        "amount": amount,
        "type": type,
        "category": category,
        "description": description,
      };

  // Parse from SQLite row
  factory Record.fromDbMap(Map<String, Object?> m) {
    return Record(
      id: m["id"] as int,
      date: m["date"] as String,
      amount: (m["amount"] as num).toDouble(),
      type: m["type"] as String,
      category: (m["category"] as String?) ?? "",
      description: (m["description"] as String?) ?? "",
    );
  }
}
