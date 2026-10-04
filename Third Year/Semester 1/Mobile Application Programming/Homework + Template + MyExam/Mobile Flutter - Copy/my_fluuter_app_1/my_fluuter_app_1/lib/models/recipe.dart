class Recipe {
  final String id;
  String title;
  String ingredients;
  String steps;
  int preparationTime;
  final DateTime dateCreated;
  bool synced;
  bool markedForDelete;

  Recipe({
    required this.id,
    required this.title,
    required this.ingredients,
    required this.steps,
    required this.preparationTime,
    required this.dateCreated,
    this.synced = false,
    this.markedForDelete = false,
  });

  Map<String, dynamic> toMap() => {
    'id': id,
    'title': title,
    'ingredients': ingredients,
    'steps': steps,
    'preparationTime': preparationTime,
    'dateCreated': dateCreated.millisecondsSinceEpoch,
    'synced': synced ? 1 : 0,
    'markedForDelete': markedForDelete ? 1 : 0,
  };

  factory Recipe.fromMap(Map<String, dynamic> m) => Recipe(
    id: m['id'],
    title: m['title'],
    ingredients: m['ingredients'],
    steps: m['steps'],
    preparationTime: m['preparationTime'],
    dateCreated: DateTime.fromMillisecondsSinceEpoch(m['dateCreated']),
    synced: (m['synced'] ?? 0) == 1,
    markedForDelete: (m['markedForDelete'] ?? 0) == 1,
  );

  Map<String, dynamic> toServerJson() => {
    'title': title,
    'ingredients': ingredients,
    'steps': steps,
    'preparationTime': preparationTime,
    'dateCreated': dateCreated.millisecondsSinceEpoch,
  };

  Recipe copyWith({
    String? title,
    String? ingredients,
    String? steps,
    int? preparationTime,
    DateTime? dateCreated,
    bool? synced,
    bool? markedForDelete,
  }) {
    return Recipe(
      id: id,
      title: title ?? this.title,
      ingredients: ingredients ?? this.ingredients,
      steps: steps ?? this.steps,
      preparationTime: preparationTime ?? this.preparationTime,
      dateCreated: dateCreated ?? this.dateCreated,
      synced: synced ?? this.synced,
      markedForDelete: markedForDelete ?? this.markedForDelete,
    );
  }
}
