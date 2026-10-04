import 'dart:async';

extension StreamExtensions<T> on Stream<T> {
  Stream<T> debounceTime(Duration duration) {
    Timer? debounceTimer;
    final controller = StreamController<T>();

    listen(
      (data) {
        debounceTimer?.cancel();
        debounceTimer = Timer(duration, () {
          controller.add(data);
        });
      },
      onError: controller.addError,
      onDone: () {
        debounceTimer?.cancel();
        controller.close();
      },
      cancelOnError: false,
    );

    return controller.stream;
  }

  Stream<S> switchMap<S>(Stream<S> Function(T) mapper) {
    StreamSubscription? innerSubscription;
    final controller = StreamController<S>();

    listen(
      (data) {
        innerSubscription?.cancel();
        innerSubscription = mapper(
          data,
        ).listen(controller.add, onError: controller.addError);
      },
      onError: controller.addError,
      onDone: () {
        innerSubscription?.cancel();
        controller.close();
      },
      cancelOnError: false,
    );

    return controller.stream;
  }
}
