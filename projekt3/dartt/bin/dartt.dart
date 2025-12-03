import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:server_nano/server_nano.dart';

void main(List<String> arguments) async {
  final server = Server();
  StreamSubscription? ss;

  server.get('/', (req, res) {
    res.sendHtmlText("<h1>Dupala!</h1>");
  });

  server.ws('/ws', (socket) {
    socket.onMessage((msg) async {
      print(msg);
      if (msg is String) {
        try {
          final proc = await Process.start('fish', ['-c', msg]);
          proc.stdout.map(utf8.decode).listen(socket.sendToAll);
          proc.stderr.map(utf8.decode).listen(socket.sendToAll);
        } catch (e) {
          socket.sendToAll(e.toString());
        }
      }
    });
    // ss?.cancel();
    // ss = Stream.periodic(
    //   Duration(seconds: 1),
    //   (i) => DateTime.now().toIso8601String(),
    // ).listen(socket.sendToAll);
  });

  for (var value in (await NetworkInterface.list(
    type: InternetAddressType.IPv4,
  ))) {
    print("${value.addresses.first.address}\n${value.name}");
  }
  ;
  print("Port WS: 5001");

  await server.listen(host: '0.0.0.0', port: 5000, wsPort: 5001);
}
