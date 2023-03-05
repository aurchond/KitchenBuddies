import 'package:first_app/screens/home_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';

import '../../data_models/meal_session_steps.dart';

class ReceivedInstructionScreen extends StatelessWidget {
  final RemoteMessage message;
  const ReceivedInstructionScreen({Key? key, required this.message}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // Map<String, dynamic> messageMap = message.notification!.toMap();
    // MealSessionSteps? thisUserSteps = MealSessionSteps.fromJson(messageMap);
    Map<String, dynamic> dataMap = message.data!;

    return Scaffold(
        appBar: AppBar(title: const Text("Received Instructions")),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            //Text((thisUserSteps?.recipeSteps)?.join("\n") ?? "")
            Text(dataMap["body"])
          ],
          ),
    );
  }
}