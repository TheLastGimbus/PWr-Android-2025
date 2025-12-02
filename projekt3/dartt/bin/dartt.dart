import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:server_nano/server_nano.dart';

Function(String message)? send;

void main(List<String> arguments) async {
  final server = Server();

  server.get('/', (req, res) {
    res.sendHtmlText("<h1>Dupala!</h1>");
  });

  server.ws('/ws', (socket) {
    socket.onMessage((msg) async {
      print(msg);
      if (msg is String) {
        try {
          final proc = await Process.start('fish', ['-c', msg]);
          proc.stdout.map(utf8.decode).listen(send);
          proc.stderr.map(utf8.decode).listen(send);
        } catch (e) {
          socket.sendToAll(e.toString());
        }
      }
    });
    Stream.periodic(
      Duration(seconds: 1),
      (i) => DateTime.now().toIso8601String(),
    ).listen(socket.sendToAll);
    send = socket.sendToAll;
  });

  await server.listen(host: '0.0.0.0', port: 5000, wsPort: 5001);
}
