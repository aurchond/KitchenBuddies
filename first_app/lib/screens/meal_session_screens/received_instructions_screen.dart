import 'package:first_app/screens/home_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';

class ReceivedInstructionScreen extends StatelessWidget {
  final RemoteMessage message;
  const ReceivedInstructionScreen({Key? key, required this.message}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return Scaffold(
        appBar: AppBar(title: const Text("Received Instructions")),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(message.data.toString()),
            //TODO: figure out how to go home
            Text(message.notification!.toMap().toString())
          ],
          ),
    );
  }
}