import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'config.dart';
import 'data.dart';

//  HELPERS
void err(BuildContext c, String m) =>
    ScaffoldMessenger.of(c).showSnackBar(SnackBar(content: Text(m)));

Widget loading(bool busy, Widget child) => busy
    ? Stack(
        children: [
          child,
          const Positioned.fill(
            child: ColoredBox(
              color: Colors.black26,
              child: Center(child: CircularProgressIndicator()),
            ),
          ),
        ],
      )
    : child;

Widget offlineBanner(bool show, VoidCallback onRetry) => show
    ? MaterialBanner(
        content: const Text('Offline'),
        actions: [TextButton(onPressed: onRetry, child: const Text('Retry'))],
      )
    : const SizedBox.shrink();

//  HOME
class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});
  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(title: Text(C.title)),
    body: Column(
      children: [
        ElevatedButton(
          onPressed: () => Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const ListScreen()),
          ),
          child: Text('View ${C.plural}'),
        ),
        ElevatedButton(
          onPressed: () => Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const ReportsScreen()),
          ),
          child: const Text('Reports'),
        ),
        ElevatedButton(
          onPressed: () => Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const InsightsScreen()),
          ),
          child: const Text('Insights'),
        ),
      ],
    ),
  );
}

//  A) LIST SCREEN
class ListScreen extends StatefulWidget {
  const ListScreen({super.key});
  @override
  State<ListScreen> createState() => _ListScreenState();
}

class _ListScreenState extends State<ListScreen> {
  bool _busy = false;
  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _busy = true);
    await context.read<Repo>().loadList();
    if (mounted) setState(() => _busy = false);
  }

  Future<void> _retry() async {
    setState(() => _busy = true);
    await context.read<Repo>().retryList();
    if (mounted) setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<Repo>();
    return Scaffold(
      appBar: AppBar(title: Text(C.plural)),
      floatingActionButton: FloatingActionButton(
        onPressed: () => Navigator.push(
          context,
          MaterialPageRoute(builder: (_) => const AddScreen()),
        ),
        child: const Icon(Icons.add),
      ),
      body: loading(
        _busy,
        Column(
          children: [
            offlineBanner(repo.offline, _retry),
            Expanded(
              child: repo.list.isEmpty
                  ? const Center(child: Text('Empty'))
                  : ListView.builder(
                      itemCount: repo.list.length,
                      itemBuilder: (_, i) {
                        final r = repo.list[i];
                        return ListTile(
                          title: Text('#${r.id} ${r.type} ${r.amount}'),
                          subtitle: Text('${r.date} ${r.category}'),
                          onTap: () => Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) => DetailsScreen(id: r.id),
                            ),
                          ),
                          trailing: IconButton(
                            icon: const Icon(Icons.delete),
                            onPressed: () => Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) => DeleteScreen(id: r.id),
                              ),
                            ),
                          ),
                        );
                      },
                    ),
            ),
          ],
        ),
      ),
    );
  }
}

//  B) DETAILS SCREEN
class DetailsScreen extends StatefulWidget {
  final int id;
  const DetailsScreen({super.key, required this.id});
  @override
  State<DetailsScreen> createState() => _DetailsScreenState();
}

class _DetailsScreenState extends State<DetailsScreen> {
  bool _busy = false;
  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _busy = true);
    await context.read<Repo>().loadDetails(widget.id);
    if (mounted) setState(() => _busy = false);
  }

  Future<void> _retry() async {
    setState(() => _busy = true);
    await context.read<Repo>().retryDetails(widget.id);
    if (mounted) setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<Repo>();
    final r = repo.details[widget.id];
    return Scaffold(
      appBar: AppBar(title: Text('${C.single} #${widget.id}')),
      body: loading(
        _busy,
        Column(
          children: [
            offlineBanner(repo.offline, _retry),
            if (r != null)
              Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('ID: ${r.id}'),
                    Text('Date: ${r.date}'),
                    Text('Amount: ${r.amount}'),
                    Text('Type: ${r.type}'),
                    Text('Category: ${r.category}'),
                    Text('Description: ${r.description}'),
                  ],
                ),
              )
            else
              const Center(child: Text('No data')),
          ],
        ),
      ),
    );
  }
}

//  C) ADD SCREEN
class AddScreen extends StatefulWidget {
  const AddScreen({super.key});
  @override
  State<AddScreen> createState() => _AddScreenState();
}

class _AddScreenState extends State<AddScreen> {
  final _date = TextEditingController(text: '2026-02-01');
  final _amount = TextEditingController(text: '5000');
  final _type = TextEditingController(text: 'salary');
  final _cat = TextEditingController(text: 'tech');
  final _desc = TextEditingController(text: 'Monthly salary');
  bool _busy = false;

  Future<void> _submit() async {
    if (_date.text.trim().isEmpty || _amount.text.trim().isEmpty ||
        _type.text.trim().isEmpty || _cat.text.trim().isEmpty ||
        _desc.text.trim().isEmpty) {
      print('[ERROR] Add: validation failed - all fields required');
      err(context, 'All fields are required!');
      return;
    }
    final amount = double.tryParse(_amount.text.trim());
    if (amount == null) {
      print('[ERROR] Add: validation failed - invalid amount "${_amount.text}"');
      err(context, 'Amount must be a number!');
      return;
    }
    setState(() => _busy = true);
    try {
      await context.read<Repo>().add(
        _date.text.trim(),
        amount,
        _type.text.trim(),
        _cat.text.trim(),
        _desc.text.trim(),
      );
      if (mounted) {
        err(context, 'Added!');
        Navigator.pop(context);
      }
    } on Offline catch (e) {
      print('[ERROR] Add: offline - $e');
      err(context, 'Online only!');
    } catch (e) {
      print('[ERROR] Add: $e');
      err(context, 'Error: $e');
    }
    if (mounted) setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(title: Text('Add ${C.single}')),
    body: loading(
      _busy,
      ListView(
        padding: const EdgeInsets.all(16),
        children: [
          TextField(
            controller: _date,
            decoration: const InputDecoration(labelText: 'Date'),
          ),
          TextField(
            controller: _amount,
            decoration: const InputDecoration(labelText: 'Amount'),
            keyboardType: TextInputType.number,
          ),
          TextField(
            controller: _type,
            decoration: const InputDecoration(labelText: 'Type'),
          ),
          TextField(
            controller: _cat,
            decoration: const InputDecoration(labelText: 'Category'),
          ),
          TextField(
            controller: _desc,
            decoration: const InputDecoration(labelText: 'Description'),
          ),
          const SizedBox(height: 16),
          ElevatedButton(onPressed: _submit, child: const Text('Create')),
        ],
      ),
    ),
  );
}

//  D) DELETE SCREEN
class DeleteScreen extends StatefulWidget {
  final int id;
  const DeleteScreen({super.key, required this.id});
  @override
  State<DeleteScreen> createState() => _DeleteScreenState();
}

class _DeleteScreenState extends State<DeleteScreen> {
  bool _busy = false;

  Future<void> _delete() async {
    setState(() => _busy = true);
    try {
      await context.read<Repo>().del(widget.id);
      if (mounted) {
        err(context, 'Deleted!');
        Navigator.pop(context);
      }
    } on Offline catch (e) {
      print('[ERROR] Delete id=${widget.id}: offline - $e');
      err(context, 'Online only!');
    } catch (e) {
      print('[ERROR] Delete id=${widget.id}: $e');
      err(context, 'Error: $e');
    }
    if (mounted) setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(title: Text('Delete #${widget.id}')),
    body: loading(
      _busy,
      Center(
        child: ElevatedButton(
          onPressed: _delete,
          child: const Text('Confirm Delete'),
        ),
      ),
    ),
  );
}

//  REPORTS SCREEN
class ReportsScreen extends StatefulWidget {
  const ReportsScreen({super.key});
  @override
  State<ReportsScreen> createState() => _ReportsScreenState();
}

class _ReportsScreenState extends State<ReportsScreen> {
  bool _busy = false;
  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _busy = true);
    await context.read<Repo>().loadAll();
    if (mounted) setState(() => _busy = false);
  }

  Future<void> _retry() async {
    setState(() => _busy = true);
    await context.read<Repo>().retryAll();
    if (mounted) setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<Repo>();
    final data = repo.monthly();
    return Scaffold(
      appBar: AppBar(title: const Text('Monthly Totals')),
      body: loading(
        _busy,
        Column(
          children: [
            offlineBanner(repo.offline, _retry),
            Expanded(
              child: ListView.builder(
                itemCount: data.length,
                itemBuilder: (_, i) => ListTile(
                  title: Text(data[i].key),
                  trailing: Text('${data[i].value}'),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

//  INSIGHTS SCREEN
class InsightsScreen extends StatefulWidget {
  const InsightsScreen({super.key});
  @override
  State<InsightsScreen> createState() => _InsightsScreenState();
}

class _InsightsScreenState extends State<InsightsScreen> {
  bool _busy = false;
  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    setState(() => _busy = true);
    await context.read<Repo>().loadAll();
    if (mounted) setState(() => _busy = false);
  }

  Future<void> _retry() async {
    setState(() => _busy = true);
    await context.read<Repo>().retryAll();
    if (mounted) setState(() => _busy = false);
  }

  @override
  Widget build(BuildContext context) {
    final repo = context.watch<Repo>();
    final data = repo.top3();
    return Scaffold(
      appBar: AppBar(title: const Text('Top 3 Categories')),
      body: loading(
        _busy,
        Column(
          children: [
            offlineBanner(repo.offline, _retry),
            Expanded(
              child: ListView.builder(
                itemCount: data.length,
                itemBuilder: (_, i) => ListTile(
                  leading: Text('#${i + 1}'),
                  title: Text(data[i].key),
                  trailing: Text('${data[i].value}'),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
